/* (C)2025 */
package ing.hub.wallet.service;

import ing.hub.wallet.api.dto.request.WalletCreateRequest;
import ing.hub.wallet.api.dto.request.WalletFilterRequest;
import ing.hub.wallet.entity.Wallet;
import ing.hub.wallet.exception.ApiException;
import ing.hub.wallet.repository.WalletRepository;
import ing.hub.wallet.specification.WalletSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletService {

	private final WalletRepository walletRepository;
	private final CustomerService customerService;

	@Transactional(readOnly = true)
	public List<Wallet> filterWallets(final WalletFilterRequest filterRequest) {
		return walletRepository.findAll(WalletSpecification.fromFilter(filterRequest));
	}

	@Transactional
	public Wallet create(final WalletCreateRequest createRequest) {

		final var customer = customerService
				.findById(createRequest.getCustomerId())
				.orElseThrow(() -> ApiException.builder()
						.errorCode("not-found")
						.message("Customer not found. Id :" + createRequest.getCustomerId())
						.build());

		final var wallet = new Wallet();
		wallet.setCustomer(customer);
		wallet.setActiveForShopping(createRequest.isActiveForShopping());
		wallet.setActiveForWithdraw(createRequest.isActiveForWithdraw());
		wallet.setBalance(BigDecimal.ZERO);
		wallet.setCurrency(createRequest.getCurrency());
		wallet.setUsableBalance(BigDecimal.ZERO);
		return walletRepository.save(wallet);
	}

	@Transactional
	public Wallet update(final Wallet wallet) {
		return walletRepository.save(wallet);
	}

	@Transactional
	public Wallet findByIdForUpdate(final UUID walletId) {
		return walletRepository
				.findOne(WalletSpecification.hasAccess(walletId))
				.map(wallet -> walletRepository.findByIdForUpdate(wallet.getId()))
				.orElseThrow(() -> ApiException.builder()
						.errorCode("not-found")
						.message("Wallet not found. ID: " + walletId)
						.build());
	}
}
