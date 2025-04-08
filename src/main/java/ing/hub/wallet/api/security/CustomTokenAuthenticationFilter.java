/* (C)2025 */
package ing.hub.wallet.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import ing.hub.wallet.api.constant.ApiConstant;
import ing.hub.wallet.api.dto.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class CustomTokenAuthenticationFilter extends OncePerRequestFilter {

	private final CustomTokenAuthenticationManager customTokenAuthenticationManager;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(
			final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
			throws ServletException, IOException {

		final var path = request.getRequestURI();
		log.info("Path: {}", path);
		if (path.contains(ApiConstant.PUBLIC_PATH)) {
			filterChain.doFilter(request, response);
			return;
		}

		final var token = request.getHeader("authentication-token");

		if (token == null || token.isBlank()) {
			log.error("=======>!!! authentication-token required !!!<====");
			sendErrorResponse(response, " authentication-token required");
			return;
		}

		final var authRequest = new CustomTokenAuthentication(token);

		try {
			final var authentication = customTokenAuthenticationManager.authenticate(authRequest);
			if (authentication.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (final AuthenticationException e) {
			log.error(e.getMessage(), e);
			sendErrorResponse(response, e.getMessage());
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void sendErrorResponse(final HttpServletResponse response, final String message) throws IOException {
		final var errorResponse = ErrorResponse.builder()
				.message(message)
				.errorCode("authentication-exception")
				.build();
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
