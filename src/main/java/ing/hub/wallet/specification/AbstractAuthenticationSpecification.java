/* (C)2025 */
package ing.hub.wallet.specification;

import ing.hub.wallet.api.dto.LoggedUser;
import ing.hub.wallet.api.security.CustomTokenAuthentication;
import ing.hub.wallet.entity.enums.Role;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class AbstractAuthenticationSpecification {

	AbstractAuthenticationSpecification() {}

	protected static LoggedUser getLoggedUser() {
		return ((CustomTokenAuthentication) SecurityContextHolder.getContext().getAuthentication()).getLoggedUser();
	}

	protected static boolean isEmployee() {
		final var user = getLoggedUser();
		return Role.EMPLOYEE.equals(user.getRole());
	}
}
