/* (C)2025 */
package ing.hub.wallet.api.security;

import ing.hub.wallet.api.dto.LoggedUser;
import ing.hub.wallet.entity.enums.Role;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CustomTokenAuthentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 579416912070814271L;
	private final UUID token;
	private final LoggedUser loggedUser;

	public CustomTokenAuthentication(final String token) {
		super(AuthorityUtils.NO_AUTHORITIES);
		this.token = UUID.fromString(token);
		this.loggedUser = null;
		setAuthenticated(false);
	}

	public CustomTokenAuthentication(final UUID token, final Role role, boolean isAuthenticated) {
		super(AuthorityUtils.createAuthorityList(List.of(role.name())));
		this.token = token;
		setAuthenticated(isAuthenticated);
		this.loggedUser = LoggedUser.builder().customerId(token).role(role).build();
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}
}
