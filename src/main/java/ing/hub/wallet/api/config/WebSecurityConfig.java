/* (C)2025 */
package ing.hub.wallet.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ing.hub.wallet.api.security.CustomTokenAuthenticationFilter;
import ing.hub.wallet.api.security.CustomTokenAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final CustomTokenAuthenticationManager customTokenAuthenticationManager;
	private final ObjectMapper objectMapper;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authz -> authz.requestMatchers(HttpMethod.GET, "/api/public/**")
						.permitAll()
						.anyRequest()
						.authenticated())
				.csrf(csrf -> csrf.disable())
				.httpBasic(httpBasic -> httpBasic.disable())
				.addFilterBefore(customTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CustomTokenAuthenticationFilter customTokenAuthenticationFilter() {
		return new CustomTokenAuthenticationFilter(customTokenAuthenticationManager, objectMapper);
	}
}
