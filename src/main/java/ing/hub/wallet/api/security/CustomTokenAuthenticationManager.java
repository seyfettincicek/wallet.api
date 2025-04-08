/* (C)2025 */
package ing.hub.wallet.api.security;

import ing.hub.wallet.service.AuthenticationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomTokenAuthenticationManager implements AuthenticationManager {

	private final AuthenticationService authenticationService;

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		final var token = UUID.fromString(authentication.getCredentials().toString());

		return authenticationService
				.authenticate(token)
				.map(customer -> new CustomTokenAuthentication(token, customer.getRole(), true))
				.orElseThrow(() -> {
					log.error("!!! Authentication failed. Invalid token: {}", token);
					return new BadCredentialsException("invalid-token");
				});
	}
}
