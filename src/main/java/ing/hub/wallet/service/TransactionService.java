/* (C)2025 */
package ing.hub.wallet.service;

import ing.hub.wallet.api.constant.Constant;
import ing.hub.wallet.api.dto.request.TransactionFilterRequest;
import ing.hub.wallet.api.dto.request.TransactionOperationRequest;
import ing.hub.wallet.api.dto.request.TransactionProcessRequest;
import ing.hub.wallet.entity.Transaction;
import ing.hub.wallet.entity.Wallet;
import ing.hub.wallet.entity.enums.OppositePartyType;
import ing.hub.wallet.entity.enums.TransactionStatus;
import ing.hub.wallet.entity.enums.TransactionType;
import ing.hub.wallet.exception.ApiException;
import ing.hub.wallet.repository.TransactionRepository;
import ing.hub.wallet.specification.TransactionSpecification;
import ing.hub.wallet.util.ApiUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

	private final TransactionRepository transactionRepository;

	private final WalletService walletService;

	@Transactional(readOnly = true)
	public List<Transaction> filterTransactions(final TransactionFilterRequest filterRequest) {
		return transactionRepository.findAll(TransactionSpecification.fromFilter(filterRequest));
	}

	@Transactional
	public Transaction withdraw(final TransactionOperationRequest withdrawRequest) {

		final var wallet = walletService.findByIdForUpdate(withdrawRequest.getWalletId());

		if (!wallet.getActiveForWithdraw().booleanValue()) {
			log.warn("Transaction denied because wallet : {} is not active for withdraw.", wallet.getId());
			return createAndSaveTransaction(
					wallet, withdrawRequest, TransactionStatus.DENIED, TransactionType.WITHDRAW);
		}

		final var transactionAmount = withdrawRequest.getAmount();

		// I am not going to check insufficient balance...

		final var transactionStatus = getTransactionStatus(transactionAmount);
		wallet.setUsableBalance(wallet.getUsableBalance().subtract(transactionAmount));
		if (transactionStatus.equals(TransactionStatus.APPROVED)) {
			wallet.setBalance(wallet.getBalance().subtract(transactionAmount));
		}

		walletService.update(wallet);
		return createAndSaveTransaction(wallet, withdrawRequest, transactionStatus, TransactionType.WITHDRAW);
	}

	@Transactional
	public Transaction deposit(final TransactionOperationRequest depositRequest) {

		final var wallet = walletService.findByIdForUpdate(depositRequest.getWalletId());
		final var transactionAmount = depositRequest.getAmount();

		wallet.setBalance(wallet.getBalance().add(transactionAmount));
		final var transactionStatus = getTransactionStatus(transactionAmount);
		if (transactionStatus.equals(TransactionStatus.APPROVED)) {
			wallet.setUsableBalance(wallet.getUsableBalance().add(transactionAmount));
		}

		walletService.update(wallet);
		return createAndSaveTransaction(wallet, depositRequest, transactionStatus, TransactionType.DEPOSIT);
	}

	private TransactionStatus getTransactionStatus(final BigDecimal amount) {
		return amount.compareTo(Constant.APPROVAL_THRESHOLD) > 0
				? TransactionStatus.PENDING
				: TransactionStatus.APPROVED;
	}

	private Transaction buildTransaction(
			final Wallet wallet,
			final TransactionOperationRequest withdrawRequest,
			final TransactionStatus transactionStatus,
			final TransactionType transactionType) {
		final var isValidIban = ApiUtils.isValidIban(withdrawRequest.getOppositeParty());

		final var transaction = new Transaction();
		transaction.setWallet(wallet);
		transaction.setAmount(withdrawRequest.getAmount());
		transaction.setOppositeParty(withdrawRequest.getOppositeParty());
		transaction.setOppositePartyType(isValidIban ? OppositePartyType.IBAN : OppositePartyType.PAYMENT);
		transaction.setStatus(transactionStatus);
		transaction.setType(transactionType);

		return transaction;
	}

	private Transaction createAndSaveTransaction(
			final Wallet wallet,
			final TransactionOperationRequest withdrawRequest,
			final TransactionStatus transactionStatus,
			final TransactionType transactionType) {
		final var transaction = buildTransaction(wallet, withdrawRequest, transactionStatus, transactionType);
		return transactionRepository.save(transaction);
	}

	@Transactional
	public Transaction process(final TransactionProcessRequest processRequest) {
		final var transaction = findByIdForUpdate(processRequest.getTransactionId());

		if (!TransactionStatus.PENDING.equals(transaction.getStatus())) {
			throw ApiException.builder()
					.errorCode("transaction-not-processable")
					.message("Only PENDING transactions can be processed. Transaction ID: " + transaction.getId()
							+ " is " + transaction.getStatus())
					.build();
		}

		final var wallet =
				walletService.findByIdForUpdate(transaction.getWallet().getId());
		final var amount = transaction.getAmount();
		final var isApproved = TransactionStatus.APPROVED.equals(processRequest.getProcess());

		if (TransactionType.DEPOSIT.equals(transaction.getType())) {
			wallet.setBalance(wallet.getBalance().subtract(amount));
			if (isApproved) {
				wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
			}
		} else {
			wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
			if (isApproved) {
				wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
				wallet.setBalance(wallet.getBalance().subtract(amount));
			}
		}

		walletService.update(wallet);
		transaction.setStatus(processRequest.getProcess());
		return transactionRepository.save(transaction);
	}

	@Transactional
	public Transaction findByIdForUpdate(final UUID transactionId) {
		return transactionRepository
				.findOne(TransactionSpecification.hasAccess(transactionId))
				.map(wallet -> transactionRepository.findByIdForUpdate(wallet.getId()))
				.orElseThrow(() -> ApiException.builder()
						.errorCode("not-found")
						.message("Transaction not found. ID: " + transactionId)
						.build());
	}
}
