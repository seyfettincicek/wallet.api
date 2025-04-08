/* (C)2025 */
package ing.hub.wallet.api.dto.response;

import ing.hub.wallet.entity.enums.Role;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerInitializeResponse {

	private final UUID id;
	private final String name;
	private final String surname;
	private final String tckn;
	private final Role role;
}
