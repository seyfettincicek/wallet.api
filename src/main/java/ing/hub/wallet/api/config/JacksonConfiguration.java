/* (C)2025 */
package ing.hub.wallet.api.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfiguration {

	@Bean
	public ObjectMapper objectMapper(final Jackson2ObjectMapperBuilder builder) {
		return builder.serializationInclusion(JsonInclude.Include.NON_NULL)
				.propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
				.modulesToInstall(
						new JavaTimeModule().addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE))
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build();
	}
}
