/* (C)2025 */
package ing.hub.wallet.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EntityScan(basePackages = "ing.hub.wallet.entity")
@EnableJpaRepositories(basePackages = "ing.hub.wallet.repository")
@SpringBootApplication(scanBasePackages = "ing.hub.wallet")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
