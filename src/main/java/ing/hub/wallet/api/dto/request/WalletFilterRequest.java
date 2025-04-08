/* (C)2025 */
package ing.hub.wallet.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ing.hub.wallet.entity.enums.Currency;
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
public class WalletFilterRequest implements Serializable {

	private static final long serialVersionUID = 4887835166144949174L;

	@NotNull
	private UUID customerId;

	private Currency currency;
	private BigDecimal minBalance;
	private BigDecimal maxBalance;
	private Boolean activeForShopping;
	private Boolean activeForWithdraw;
}
