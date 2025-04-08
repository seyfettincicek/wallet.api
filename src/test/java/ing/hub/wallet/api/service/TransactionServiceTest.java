/* (C)2025 */
package ing.hub.wallet.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ing.hub.wallet.api.dto.LoggedUser;
import ing.hub.wallet.api.dto.request.TransactionFilterRequest;
import ing.hub.wallet.api.dto.request.TransactionOperationRequest;
import ing.hub.wallet.api.dto.request.TransactionProcessRequest;
import ing.hub.wallet.api.security.CustomTokenAuthentication;
import ing.hub.wallet.entity.Transaction;
import ing.hub.wallet.entity.Wallet;
import ing.hub.wallet.entity.enums.Role;
import ing.hub.wallet.entity.enums.TransactionStatus;
import ing.hub.wallet.entity.enums.TransactionType;
import ing.hub.wallet.exception.ApiException;
import ing.hub.wallet.repository.TransactionRepository;
import ing.hub.wallet.service.TransactionService;
import ing.hub.wallet.service.WalletService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private WalletService walletService;

	@InjectMocks
	private TransactionService transactionService;

	@Mock
	private CustomTokenAuthentication authentication;

	@Mock
	private LoggedUser loggedUser;

	@BeforeEach
	void setup() {
		SecurityContextHolder.clearContext();
		final var context = new SecurityContextImpl();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
		lenient().when(authentication.getLoggedUser()).thenReturn(loggedUser);
	}

	@Test
	void testShouldVerifyWithdrawApproved() {
		final var wallet = walletWith(true, new BigDecimal("1000"), new BigDecimal("1000"));
		final var request = transactionRequest(wallet.getId(), new BigDecimal("500"));

		when(walletService.findByIdForUpdate(wallet.getId())).thenReturn(wallet);
		when(transactionRepository.save(any())).thenReturn(new Transaction());

		final var result = transactionService.withdraw(request);

		assertNotNull(result);
		assertEquals(new BigDecimal("500"), wallet.getBalance());
		assertEquals(new BigDecimal("500"), wallet.getUsableBalance());
		verify(walletService).update(wallet);
	}

	@Test
	void testShouldVerifyWithdrawDenied() {
		final var wallet = walletWith(false, new BigDecimal("1000"), new BigDecimal("1000"));
		final var request = transactionRequest(wallet.getId(), new BigDecimal("500"));

		when(walletService.findByIdForUpdate(wallet.getId())).thenReturn(wallet);
		when(transactionRepository.save(any())).thenReturn(new Transaction());

		final var result = transactionService.withdraw(request);

		assertNotNull(result);
		verify(walletService, never()).update(wallet);
	}

	@Test
	void testShouldVerifyDepositApproved() {
		final var wallet = walletWith(true, new BigDecimal("1000"), new BigDecimal("1000"));
		final var request = transactionRequest(wallet.getId(), new BigDecimal("500"));

		when(walletService.findByIdForUpdate(wallet.getId())).thenReturn(wallet);
		when(transactionRepository.save(any())).thenReturn(new Transaction());

		final var result = transactionService.deposit(request);

		assertNotNull(result);
		assertEquals(new BigDecimal("1500"), wallet.getBalance());
		assertEquals(new BigDecimal("1500"), wallet.getUsableBalance());
		verify(walletService).update(wallet);
	}

	@Test
	void testShouldVerifyDepositPending() {
		final var wallet = walletWith(true, new BigDecimal("1000"), new BigDecimal("1000"));
		final var request = transactionRequest(wallet.getId(), new BigDecimal("10001"));

		when(walletService.findByIdForUpdate(wallet.getId())).thenReturn(wallet);
		when(transactionRepository.save(any())).thenReturn(new Transaction());

		final var result = transactionService.deposit(request);

		assertNotNull(result);
		assertEquals(new BigDecimal("11001"), wallet.getBalance());
		assertEquals(new BigDecimal("1000"), wallet.getUsableBalance());
		verify(walletService).update(wallet);
	}

	@Test
	void testShouldVerifyWithdrawPending() {
		final var wallet = walletWith(true, new BigDecimal("10001"), new BigDecimal("10001"));
		final var request = transactionRequest(wallet.getId(), new BigDecimal("10001"));

		when(walletService.findByIdForUpdate(wallet.getId())).thenReturn(wallet);
		when(transactionRepository.save(any())).thenReturn(new Transaction());

		final var result = transactionService.withdraw(request);

		assertNotNull(result);
		assertEquals(new BigDecimal("10001"), wallet.getBalance());
		assertEquals(BigDecimal.ZERO, wallet.getUsableBalance());
		verify(walletService).update(wallet);
	}

	@Test
	void testShouldVerifyProcessApprovedDeposit() {
		final var wallet = walletWith(true, new BigDecimal("1000"), new BigDecimal("500"));
		final var transaction =
				transaction(wallet, TransactionStatus.PENDING, TransactionType.DEPOSIT, new BigDecimal("500"));
		final var request = new TransactionProcessRequest(transaction.getId(), TransactionStatus.APPROVED);

		when(transactionRepository.findOne(any(Specification.class))).thenReturn(Optional.of(transaction));
		when(transactionRepository.findByIdForUpdate(transaction.getId())).thenReturn(transaction);
		when(walletService.findByIdForUpdate(wallet.getId())).thenReturn(wallet);
		when(transactionRepository.save(any())).thenReturn(transaction);

		final var result = transactionService.process(request);

		assertNotNull(result);
		assertEquals(new BigDecimal("500"), wallet.getBalance());
		assertEquals(new BigDecimal("1000"), wallet.getUsableBalance());
	}

	@Test
	void testShouldVerifyProcessApprovedWithdraw() {
		final var wallet = walletWith(true, new BigDecimal("1500"), new BigDecimal("1000"));
		final var transaction =
				transaction(wallet, TransactionStatus.PENDING, TransactionType.WITHDRAW, new BigDecimal("500"));
		final var request = new TransactionProcessRequest(transaction.getId(), TransactionStatus.APPROVED);

		when(transactionRepository.findOne(any(Specification.class))).thenReturn(Optional.of(transaction));
		when(transactionRepository.findByIdForUpdate(transaction.getId())).thenReturn(transaction);
		when(walletService.findByIdForUpdate(wallet.getId())).thenReturn(wallet);
		when(transactionRepository.save(any())).thenReturn(transaction);

		final var result = transactionService.process(request);

		assertNotNull(result);
		assertEquals(new BigDecimal("1000"), wallet.getBalance());
		assertEquals(new BigDecimal("1000"), wallet.getUsableBalance());
	}

	@Test
	void testShouldFailWhenTransactionNotFound() {
		final var request = new TransactionProcessRequest(UUID.randomUUID(), TransactionStatus.APPROVED);

		when(transactionRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());

		final var ex = assertThrows(ApiException.class, () -> transactionService.process(request));
		assertEquals("not-found", ex.getErrorCode());
	}

	@Test
	void testShouldFailWhenTransactionNotPending() {
		final var wallet = walletWith(true, new BigDecimal("1000"), new BigDecimal("1000"));
		final var transaction =
				transaction(wallet, TransactionStatus.APPROVED, TransactionType.DEPOSIT, new BigDecimal("100"));
		final var request = new TransactionProcessRequest(transaction.getId(), TransactionStatus.APPROVED);

		when(transactionRepository.findOne(any(Specification.class))).thenReturn(Optional.of(transaction));
		when(transactionRepository.findByIdForUpdate(transaction.getId())).thenReturn(transaction);

		final var ex = assertThrows(ApiException.class, () -> transactionService.process(request));
		assertEquals("transaction-not-processable", ex.getErrorCode());
	}

	@Test
	void testShouldVerifyFilterTransactionsForEmployee() {
		when(loggedUser.getRole()).thenReturn(Role.EMPLOYEE);
		when(transactionRepository.findAll(any(Specification.class))).thenReturn(List.of(new Transaction()));

		final var result = transactionService.filterTransactions(new TransactionFilterRequest());

		assertEquals(1, result.size());
	}

	private TransactionOperationRequest transactionRequest(final UUID walletId, final BigDecimal amount) {
		final var request = new TransactionOperationRequest();
		request.setWalletId(walletId);
		request.setAmount(amount);
		request.setOppositeParty("TR000000000000000000000000");
		return request;
	}

	private Wallet walletWith(
			final boolean activeForWithdraw, final BigDecimal balance, final BigDecimal usableBalance) {
		final var wallet = new Wallet();
		wallet.setId(UUID.randomUUID());
		wallet.setActiveForWithdraw(activeForWithdraw);
		wallet.setBalance(balance);
		wallet.setUsableBalance(usableBalance);
		return wallet;
	}

	private Transaction transaction(
			final Wallet wallet, final TransactionStatus status, final TransactionType type, final BigDecimal amount) {
		final var t = new Transaction();
		t.setId(UUID.randomUUID());
		t.setWallet(wallet);
		t.setStatus(status);
		t.setType(type);
		t.setAmount(amount);
		return t;
	}
}
