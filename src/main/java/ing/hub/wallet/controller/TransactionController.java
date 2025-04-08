/* (C)2025 */
package ing.hub.wallet.controller;

import ing.hub.wallet.api.constant.ApiConstant;
import ing.hub.wallet.api.dto.TransactionDto;
import ing.hub.wallet.api.dto.request.TransactionFilterRequest;
import ing.hub.wallet.api.dto.request.TransactionOperationRequest;
import ing.hub.wallet.api.dto.request.TransactionProcessRequest;
import ing.hub.wallet.api.dto.response.TransactionResponse;
import ing.hub.wallet.entity.enums.TransactionStatus;
import ing.hub.wallet.exception.ApiException;
import ing.hub.wallet.mapper.TransactionMapper;
import ing.hub.wallet.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiConstant.TRANSACTION_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

	private final TransactionService transactionService;

	private static final List<TransactionStatus> PROCESSABLE_TRANSACTION_STATUSES =
			List.of(TransactionStatus.APPROVED, TransactionStatus.DENIED);

	@PostMapping(path = ApiConstant.FILTER)
	@Operation(summary = "Filter transactions", description = "Filters transactions by wallet ID and status")
	public List<TransactionDto> filterTransactions(@Valid @RequestBody final TransactionFilterRequest filterRequest) {
		return transactionService.filterTransactions(filterRequest).stream()
				.map(TransactionMapper::toDto)
				.toList();
	}

	@PostMapping(path = ApiConstant.WITHDRAW)
	public TransactionResponse withdraw(@Valid @RequestBody final TransactionOperationRequest withdrawRequest) {
		return TransactionMapper.toResponse(transactionService.withdraw(withdrawRequest));
	}

	@PostMapping(path = ApiConstant.DEPOSIT)
	public TransactionResponse deposit(@Valid @RequestBody final TransactionOperationRequest depositRequest) {
		return TransactionMapper.toResponse(transactionService.deposit(depositRequest));
	}

	@PutMapping(path = ApiConstant.PROCESS)
	public TransactionResponse process(@Valid @RequestBody final TransactionProcessRequest processRequest) {
		if (PROCESSABLE_TRANSACTION_STATUSES.contains(processRequest.getProcess())) {
			return TransactionMapper.toResponse(transactionService.process(processRequest));
		} else {
			throw ApiException.builder()
					.errorCode("invalid-transaction-process")
					.message("Process that " + processRequest.getProcess() + " not accepted. Try one of them : "
							+ PROCESSABLE_TRANSACTION_STATUSES)
					.build();
		}
	}
}
