/* (C)2025 */
package ing.hub.wallet.specification;

import ing.hub.wallet.api.dto.request.TransactionFilterRequest;
import ing.hub.wallet.entity.Transaction;
import ing.hub.wallet.entity.enums.TransactionStatus;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public final class TransactionSpecification extends AbstractAuthenticationSpecification {

	public static Specification<Transaction> fromFilter(final TransactionFilterRequest filter) {

		var spec = Specification.where(walletIdEquals(filter.getWalletId()));

		if (filter.getStatus() != null) {
			spec = spec.and(statusEquals(filter.getStatus()));
		}

		if (!isEmployee()) {
			spec.and(isCustomerWallet());
		}

		return spec;
	}

	private static Specification<Transaction> walletIdEquals(final UUID walletId) {
		return (root, query, criteriaBuilder) ->
				criteriaBuilder.equal(root.get("wallet").get("id"), walletId);
	}

	private static Specification<Transaction> statusEquals(final TransactionStatus status) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
	}

	private static Specification<Transaction> isCustomerWallet() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
				root.get("wallet").get("customer").get("id"), getLoggedUser().getCustomerId());
	}

	public static Specification<Transaction> hasAccess(final UUID walletId) {

		var spec = Specification.where(walletIdEquals(walletId));

		if (!isEmployee()) {
			spec.and(isCustomerWallet());
		}

		return spec;
	}
}
