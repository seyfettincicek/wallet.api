/* (C)2025 */
package ing.hub.wallet.entity;

import ing.hub.wallet.entity.enums.OppositePartyType;
import ing.hub.wallet.entity.enums.TransactionStatus;
import ing.hub.wallet.entity.enums.TransactionType;
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
@Table(name = "transaction")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(doNotUseGetters = true, callSuper = true)
public class Transaction extends BaseEntity {

	private static final long serialVersionUID = -6634597841465309997L;

	@ManyToOne
	private Wallet wallet;

	@Column
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	private TransactionType type;

	@Enumerated(EnumType.STRING)
	private OppositePartyType oppositePartyType;

	@Column
	private String oppositeParty;

	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
}
