/* (C)2025 */
package ing.hub.wallet.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

	private static final String AUTHENTICATION_TOKEN_NAME = "authentication-token";

	@Bean
	public OpenAPI apiInfo() {
		return new OpenAPI()
				.info(new Info()
						.title("Wallet API")
						.description("API documentation for Wallet application")
						.version("1.0.0"))
				.addSecurityItem(new SecurityRequirement().addList(AUTHENTICATION_TOKEN_NAME))
				.components(new Components()
						.addSecuritySchemes(
								AUTHENTICATION_TOKEN_NAME,
								new SecurityScheme()
										.name(AUTHENTICATION_TOKEN_NAME)
										.type(SecurityScheme.Type.APIKEY)
										.in(SecurityScheme.In.HEADER)
										.description("Enter the custom authentication token in the header")));
	}
}
