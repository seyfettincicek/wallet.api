/* (C)2025 */
package ing.hub.wallet.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

	private final String errorCode;
	private final String message;
}
