/* (C)2025 */
package ing.hub.wallet.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TransactionOperationRequest implements Serializable {

	private static final long serialVersionUID = 1297646862690908489L;

	@NotNull
	private UUID walletId;

	@NotNull
	private BigDecimal amount;

	@NotBlank
	private String oppositeParty;
}
