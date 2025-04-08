/* (C)2025 */
package ing.hub.wallet.mapper;

import ing.hub.wallet.api.dto.TransactionDto;
import ing.hub.wallet.api.dto.response.TransactionResponse;
import ing.hub.wallet.entity.Transaction;

public final class TransactionMapper {
	private TransactionMapper() {
		// TransactionMapper
	}

	public static TransactionDto toDto(final Transaction transaction) {

		return TransactionDto.builder()
				.id(transaction.getId())
				.amount(transaction.getAmount())
				.oppositeParty(transaction.getOppositeParty())
				.oppositePartyType(transaction.getOppositePartyType())
				.status(transaction.getStatus())
				.type(transaction.getType())
				.build();
	}

	public static TransactionResponse toResponse(final Transaction transaction) {
		return TransactionResponse.builder()
				.transactionId(transaction.getId())
				.status(transaction.getStatus())
				.type(transaction.getType())
				.amount(transaction.getAmount())
				.build();
	}
}
