/* (C)2025 */
package ing.hub.wallet.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ing.hub.wallet.entity.enums.TransactionStatus;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
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
public class TransactionProcessRequest implements Serializable {

	private static final long serialVersionUID = 6810104687836394968L;

	@NotNull
	private UUID transactionId;

	@NotNull
	private TransactionStatus process;
}
