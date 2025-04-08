/* (C)2025 */
package ing.hub.wallet.specification;

import ing.hub.wallet.api.dto.request.WalletFilterRequest;
import ing.hub.wallet.entity.Wallet;
import ing.hub.wallet.entity.enums.Currency;
import ing.hub.wallet.exception.ApiException;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class WalletSpecification extends AbstractAuthenticationSpecification {

	private WalletSpecification() {
		// WalletSpecification
	}

	public static Specification<Wallet> fromFilter(final WalletFilterRequest filter) {

		if (!isEmployee() && !getLoggedUser().getCustomerId().equals(filter.getCustomerId())) {
			throw ApiException.builder()
					.errorCode("illegal-attempt-exception")
					.message("Something is wrong !!!")
					.build();
		}

		var spec = Specification.where(customerIdEquals(filter.getCustomerId()));

		if (filter.getCurrency() != null) {
			spec = spec.and(currencyEquals(filter.getCurrency()));
		}
		if (filter.getMinBalance() != null) {
			spec = spec.and(minBalance(filter.getMinBalance()));
		}
		if (filter.getMaxBalance() != null) {
			spec = spec.and(maxBalance(filter.getMaxBalance()));
		}
		if (filter.getActiveForShopping() != null) {
			spec = spec.and(activeForShopping(filter.getActiveForShopping()));
		}
		if (filter.getActiveForWithdraw() != null) {
			spec = spec.and(activeForWithdraw(filter.getActiveForWithdraw()));
		}

		return spec;
	}

	private static Specification<Wallet> customerIdEquals(final UUID customerId) {
		return (root, query, cb) -> cb.equal(root.get("customer").get("id"), customerId);
	}

	private static Specification<Wallet> currencyEquals(final Currency currency) {
		return (root, query, cb) -> cb.equal(root.get("currency"), currency);
	}

	private static Specification<Wallet> minBalance(final BigDecimal minBalance) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("balance"), minBalance);
	}

	private static Specification<Wallet> maxBalance(final BigDecimal maxBalance) {
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("balance"), maxBalance);
	}

	private static Specification<Wallet> activeForShopping(final Boolean isActiveForShopping) {
		return (root, query, cb) -> cb.equal(root.get("activeForShopping"), isActiveForShopping);
	}

	private static Specification<Wallet> activeForWithdraw(final Boolean isActiveForWithdraw) {
		return (root, query, cb) -> cb.equal(root.get("activeForWithdraw"), isActiveForWithdraw);
	}

	private static Specification<Wallet> idEquals(final UUID walletId) {
		return (root, query, cb) -> cb.equal(root.get("id"), walletId);
	}

	public static Specification<Wallet> hasAccess(final UUID walletId) {
		var spec = Specification.where(idEquals(walletId));

		if (!isEmployee()) {
			spec = spec.and(customerIdEquals(getLoggedUser().getCustomerId()));
		}
		return spec;
	}
}
