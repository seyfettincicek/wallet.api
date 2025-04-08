/* (C)2025 */
package ing.hub.wallet.exception.handler;

import ing.hub.wallet.api.dto.response.ErrorResponse;
import ing.hub.wallet.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(final ApiException e) {
		log.error(e.getMessage(), e);
		return buildErrorResponse(e.getMessage(), e.getErrorCode(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(final Exception e) {
		log.error(e.getMessage(), e);
		return buildErrorResponse(e.getMessage(), "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleException(
			final HttpMessageNotReadableException e, final HttpServletRequest request) {
		log.error(e.getMessage(), e);
		if (!HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
			final var wrappedRequest = new ContentCachingRequestWrapper(request);
			final var content = wrappedRequest.getContentAsByteArray();
			final var body = new String(content, StandardCharsets.UTF_8);
			log.info(" Body: {}", body);
		}

		return buildErrorResponse(e.getMessage(), "BAD_REQUEST", HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(
			final String message, final String errorCode, final HttpStatus status) {
		final var errorResponse =
				ErrorResponse.builder().errorCode(errorCode).message(message).build();
		return new ResponseEntity<>(errorResponse, status);
	}
}
