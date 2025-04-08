/* (C)2025 */
package ing.hub.wallet.entity;

import ing.hub.wallet.entity.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(doNotUseGetters = true, callSuper = true)
public class Customer extends BaseEntity {

	private static final long serialVersionUID = -1518708184135489087L;

	@Column
	private String name;

	@Column
	private String surname;

	@Column(length = 10, nullable = false)
	private String tckn;

	@Enumerated(EnumType.STRING)
	private Role role;
}
