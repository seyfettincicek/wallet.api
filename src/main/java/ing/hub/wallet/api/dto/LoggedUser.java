/* (C)2025 */
package ing.hub.wallet.api.dto;

import ing.hub.wallet.entity.enums.Role;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoggedUser {
	private UUID customerId;
	private Role role;
}
