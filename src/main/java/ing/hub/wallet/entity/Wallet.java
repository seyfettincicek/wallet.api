/* (C)2025 */
package ing.hub.wallet.entity;

import ing.hub.wallet.entity.enums.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "wallet")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(doNotUseGetters = true, callSuper = true)
public class Wallet extends BaseEntity {

	private static final long serialVersionUID = 5775504551779665419L;

	@ManyToOne
	private Customer customer;

	@Column(nullable = false)
	private String walletName;

	@Enumerated(EnumType.STRING)
	private Currency currency;

	@Column(nullable = false)
	private Boolean activeForShopping = Boolean.TRUE;

	@Column(nullable = false)
	private Boolean activeForWithdraw = Boolean.TRUE;

	@Column(nullable = false)
	private BigDecimal balance = BigDecimal.ZERO;

	@Column(nullable = false)
	private BigDecimal usableBalance = BigDecimal.ZERO;
}
