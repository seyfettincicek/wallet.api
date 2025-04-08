/* (C)2025 */
package ing.hub.wallet.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ing.hub.wallet.api.dto.LoggedUser;
import ing.hub.wallet.api.dto.request.WalletCreateRequest;
import ing.hub.wallet.api.dto.request.WalletFilterRequest;
import ing.hub.wallet.api.security.CustomTokenAuthentication;
import ing.hub.wallet.entity.Customer;
import ing.hub.wallet.entity.Wallet;
import ing.hub.wallet.entity.enums.Currency;
import ing.hub.wallet.entity.enums.Role;
import ing.hub.wallet.exception.ApiException;
import ing.hub.wallet.repository.WalletRepository;
import ing.hub.wallet.service.CustomerService;
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

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

	@Mock
	private WalletRepository walletRepository;

	@Mock
	private CustomerService customerService;

	@Mock
	private LoggedUser loggedUser;

	@InjectMocks
	private WalletService walletService;

	@BeforeEach
	void setUp() {
		final var authentication = mock(CustomTokenAuthentication.class);
		lenient().when(authentication.getLoggedUser()).thenReturn(loggedUser);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	void testShouldVerifyFilterWallets() {
		final var filterRequest = new WalletFilterRequest();
		final var wallet = new Wallet();
		wallet.setId(UUID.randomUUID());
		final var wallets = List.of(wallet);
		when(loggedUser.getRole()).thenReturn(Role.EMPLOYEE);
		when(walletRepository.findAll(any(Specification.class))).thenReturn(wallets);

		final var result = walletService.filterWallets(filterRequest);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(wallet.getId(), result.get(0).getId());
	}

	@Test
	void testShouldVerifyCreateWallet() {
		final var customer = new Customer();
		customer.setId(UUID.randomUUID());

		final var createRequest = new WalletCreateRequest();
		createRequest.setCustomerId(customer.getId());
		createRequest.setCurrency(Currency.USD);
		createRequest.setActiveForShopping(true);
		createRequest.setActiveForWithdraw(true);

		when(customerService.findById(customer.getId())).thenReturn(Optional.of(customer));
		when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

		final var wallet = walletService.create(createRequest);

		assertNotNull(wallet);
		assertEquals(customer.getId(), wallet.getCustomer().getId());
		assertEquals(Currency.USD, wallet.getCurrency());
		assertTrue(wallet.getActiveForShopping());
		assertTrue(wallet.getActiveForWithdraw());
		assertEquals(BigDecimal.ZERO, wallet.getBalance());
		assertEquals(BigDecimal.ZERO, wallet.getUsableBalance());
	}

	@Test
	void testShouldFailWhenCustomerNotFoundForCreate() {
		final var createRequest = new WalletCreateRequest();
		createRequest.setCustomerId(UUID.randomUUID());

		when(customerService.findById(createRequest.getCustomerId())).thenReturn(Optional.empty());

		final var exception = assertThrows(ApiException.class, () -> walletService.create(createRequest));

		assertEquals("not-found", exception.getErrorCode());
		assertEquals("Customer not found. Id :" + createRequest.getCustomerId(), exception.getMessage());
	}

	@Test
	void testShouldVerifyUpdateWallet() {
		final var wallet = new Wallet();
		wallet.setId(UUID.randomUUID());
		wallet.setBalance(new BigDecimal("100.00"));
		wallet.setUsableBalance(new BigDecimal("50.00"));

		when(walletRepository.save(wallet)).thenReturn(wallet);

		final var updatedWallet = walletService.update(wallet);

		assertNotNull(updatedWallet);
		assertEquals(wallet.getId(), updatedWallet.getId());
		assertEquals(new BigDecimal("100.00"), updatedWallet.getBalance());
		assertEquals(new BigDecimal("50.00"), updatedWallet.getUsableBalance());
	}

	@Test
	void testShouldVerifyFindByIdForUpdate() {
		final var walletId = UUID.randomUUID();
		final var wallet = new Wallet();
		wallet.setId(walletId);

		when(walletRepository.findOne(any(Specification.class))).thenReturn(Optional.of(wallet));
		when(walletRepository.findByIdForUpdate(walletId)).thenReturn(wallet);

		final var result = walletService.findByIdForUpdate(walletId);

		assertNotNull(result);
		assertEquals(walletId, result.getId());
	}

	@Test
	void testShouldFailWhenWalletNotFoundForFindByIdForUpdate() {
		final var walletId = UUID.randomUUID();

		when(walletRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());

		final var exception = assertThrows(ApiException.class, () -> walletService.findByIdForUpdate(walletId));

		assertEquals("not-found", exception.getErrorCode());
		assertEquals("Wallet not found. ID: " + walletId, exception.getMessage());
	}
}
