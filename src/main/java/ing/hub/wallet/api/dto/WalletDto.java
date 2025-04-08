/* (C)2025 */
package ing.hub.wallet.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ing.hub.wallet.entity.enums.Currency;
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
public class WalletDto implements Serializable {

	private static final long serialVersionUID = 8082506350985012026L;

	private UUID id;
	private UUID customerId;
	private String walletName;
	private Currency currency;
	private Boolean activeForShopping;
	private Boolean activeForWithdraw;
	private BigDecimal balance;
	private BigDecimal usableBalance;
}
