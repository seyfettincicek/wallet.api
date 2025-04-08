/* (C)2025 */
package ing.hub.wallet.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = -6959511104193690289L;

	private final String errorCode;
	private final String message;
}
