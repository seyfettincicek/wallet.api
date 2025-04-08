/* (C)2025 */
package ing.hub.wallet.mapper;

import ing.hub.wallet.api.dto.response.CustomerInitializeResponse;
import ing.hub.wallet.entity.Customer;

public final class CustomerMapper {
	private CustomerMapper() {
		// CustomerMapper
	}

	public static CustomerInitializeResponse toResponse(final Customer customer) {
		return CustomerInitializeResponse.builder()
				.id(customer.getId())
				.name(customer.getName())
				.surname(customer.getSurname())
				.role(customer.getRole())
				.tckn(customer.getTckn())
				.build();
	}
}
