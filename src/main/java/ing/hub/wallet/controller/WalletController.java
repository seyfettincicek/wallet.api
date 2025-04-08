/* (C)2025 */
package ing.hub.wallet.controller;

import ing.hub.wallet.api.constant.ApiConstant;
import ing.hub.wallet.api.dto.WalletDto;
import ing.hub.wallet.api.dto.request.WalletCreateRequest;
import ing.hub.wallet.api.dto.request.WalletFilterRequest;
import ing.hub.wallet.mapper.WalletMapper;
import ing.hub.wallet.service.WalletService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiConstant.WALLET_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class WalletController {

	private final WalletService walletService;

	@PostMapping(path = ApiConstant.CREATE)
	public WalletDto getWalletsByCustmterId(@Valid @RequestBody WalletCreateRequest request) {
		return WalletMapper.toDto(walletService.create(request));
	}

	@PostMapping(path = ApiConstant.FILTER)
	public List<WalletDto> getWalletsByCustmterId(@Valid @RequestBody WalletFilterRequest filterRequest) {
		return walletService.filterWallets(filterRequest).stream()
				.map(WalletMapper::toDto)
				.toList();
	}
}
