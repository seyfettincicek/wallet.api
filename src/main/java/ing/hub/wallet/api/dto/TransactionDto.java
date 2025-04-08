/* (C)2025 */
package ing.hub.wallet.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ing.hub.wallet.entity.enums.OppositePartyType;
import ing.hub.wallet.entity.enums.TransactionStatus;
import ing.hub.wallet.entity.enums.TransactionType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto implements Serializable {

	private static final long serialVersionUID = -8673984027900008785L;

	private UUID id;
	private BigDecimal amount;
	private TransactionType type;
	private OppositePartyType oppositePartyType;
	private String oppositeParty;
	private TransactionStatus status;
}
