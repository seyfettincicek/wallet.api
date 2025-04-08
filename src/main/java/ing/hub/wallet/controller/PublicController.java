/* (C)2025 */
package ing.hub.wallet.controller;

import ing.hub.wallet.api.constant.ApiConstant;
import ing.hub.wallet.api.dto.response.CustomerInitializeResponse;
import ing.hub.wallet.mapper.CustomerMapper;
import ing.hub.wallet.service.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiConstant.PUBLIC_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicController {

	private final CustomerService customerService;

	@GetMapping(path = ApiConstant.INITIALIZE)
	public List<CustomerInitializeResponse> initialize() {
		customerService.initialize();
		return customerService.findAll().stream()
				.map(CustomerMapper::toResponse)
				.toList();
	}
}
