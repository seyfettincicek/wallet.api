/* (C)2025 */
package ing.hub.wallet.api.dto.response;

import ing.hub.wallet.entity.enums.TransactionStatus;
import ing.hub.wallet.entity.enums.TransactionType;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponse {

	private final UUID transactionId;
	private final TransactionType type;
	private final TransactionStatus status;
	private final BigDecimal amount;
}
