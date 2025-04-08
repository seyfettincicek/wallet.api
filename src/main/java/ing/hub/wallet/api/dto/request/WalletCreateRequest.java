/* (C)2025 */
package ing.hub.wallet.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ing.hub.wallet.entity.enums.Currency;
import jakarta.validation.constraints.NotBlank;
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
public class WalletCreateRequest implements Serializable {

	private static final long serialVersionUID = 198859076922587842L;

	@NotNull
	private UUID customerId;

	@NotNull
	private Currency currency;

	@NotBlank
	private String walletName;

	private boolean activeForShopping;
	private boolean activeForWithdraw;
}
