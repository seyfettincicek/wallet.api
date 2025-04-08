/* (C)2025 */
package ing.hub.wallet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ing.hub.wallet.api.dto.request.TransactionFilterRequest;
import ing.hub.wallet.entity.Customer;
import ing.hub.wallet.entity.Wallet;
import ing.hub.wallet.entity.enums.Currency;
import ing.hub.wallet.entity.enums.Role;
import ing.hub.wallet.entity.enums.TransactionStatus;
import ing.hub.wallet.repository.CustomerRepository;
import ing.hub.wallet.repository.TransactionRepository;
import ing.hub.wallet.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

	private static final int RANDOM_STRING_LENGTH = 5;
	private static final int RANDOM_NUMERIC_LENGTH = 10;

	private final CustomerRepository customerRepository;
	private final WalletRepository walletRepository;
	private final TransactionRepository transactionRepository;
	private final ObjectMapper objectMapper;

	@Transactional(readOnly = true)
	public Optional<Customer> findById(final UUID customerId) {
		return customerRepository.findById(customerId);
	}

	@Transactional
	public void initialize() {

		final var test = TransactionFilterRequest.builder()
				.status(TransactionStatus.PENDING)
				.walletId(UUID.randomUUID())
				.build();

		try {
			log.info(objectMapper.writeValueAsString(test));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		transactionRepository.deleteAll();
		walletRepository.deleteAll();
		customerRepository.deleteAll();
		createSampleData(3, Role.EMPLOYEE);
		createSampleData(3, Role.CUSTOMER);
	}

	@Transactional(readOnly = true)
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	private void createSampleData(final int count, final Role role) {

		for (var i = 1; i <= count; i++) {
			var customer = new Customer();
			customer.setName(role.name() + " " + i);
			customer.setSurname(nextAlphabetic());
			customer.setTckn(nextNumeric());
			customer.setRole(role);

			customer = customerRepository.save(customer);

			createWallet(customer, Currency.TRY, true, true);
			createWallet(customer, Currency.USD, true, false);
			createWallet(customer, Currency.EUR, false, false);
		}
	}

	private void createWallet(
			final Customer customer, final Currency currency, final boolean shopping, final boolean withdraw) {
		final var wallet = new Wallet();
		wallet.setCustomer(customer);
		wallet.setActiveForShopping(shopping);
		wallet.setActiveForWithdraw(withdraw);
		wallet.setBalance(BigDecimal.ZERO);
		wallet.setUsableBalance(BigDecimal.ZERO);
		wallet.setCurrency(currency);
		wallet.setWalletName(customer.getName() + " " + currency + " Wallet");

		walletRepository.save(wallet);
	}

	private String nextAlphabetic() {
		return RandomStringUtils.secure().nextAlphabetic(RANDOM_STRING_LENGTH);
	}

	private String nextNumeric() {
		return RandomStringUtils.secure().nextNumeric(RANDOM_NUMERIC_LENGTH);
	}
}
