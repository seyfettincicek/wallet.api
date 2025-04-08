/* (C)2025 */
package ing.hub.wallet.service;

import ing.hub.wallet.entity.Customer;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final CustomerService customerService;

	@Transactional(readOnly = true)
	public Optional<Customer> authenticate(final UUID authenticationToken) {
		return customerService.findById(authenticationToken);
	}
}
