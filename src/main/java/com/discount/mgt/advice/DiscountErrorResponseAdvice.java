package com.discount.mgt.advice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.discount.mgt.dto.DiscountErrorResponse;
import com.discount.mgt.exception.DiscountRuleException;

/**
 * Allows to handle all expected and unexpected errors occurred while processing
 * the request.
 *
 * @author Nava Krishna
 * @see ControllerAdvice
 * @since 1.0.0
 */

@ControllerAdvice
public class DiscountErrorResponseAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscountErrorResponseAdvice.class);

	private static final String APPLICATION_PROBLEM_JSON = "application/problem+json";

	/**
	 * Handles Exception when method argument is not the expected type.
	 *
	 * @param ex
	 *            {@link MethodArgumentTypeMismatchException}
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return {@link ResponseEntity} containing standard body in case of errors
	 */
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public HttpEntity<DiscountErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpServletRequest request) {

		String errorMessage = String.format("Incorrect value '%s' for field '%s'. Expected value of type '%s'",
				ex.getValue(), ex.getName(), ex.getParameter().getParameterType().getTypeName());

		DiscountErrorResponse error = new DiscountErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), null,
				"Parameter type mismatch", errorMessage, request.getRequestURI());

		return new ResponseEntity<>(error, overrideContentType(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	/**
	 * Handles the exception when business rule execution failed.
	 *
	 * @param ex{@link
	 * 			DiscountRuleException}
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return {@link ResponseEntity} containing standard body in case of errors
	 */
	@ExceptionHandler({ DiscountRuleException.class })
	public ResponseEntity<DiscountErrorResponse> handleDiscruleException(DiscountRuleException ex,
			HttpServletRequest request) {

		LOGGER.debug("Execution of rule has been failed for {} because of {} ", ex.getResourceId(),
				ex.getResourceType());

		String errorMessage = String.format("Eexecution failed for rule %s for either of productIds '%s' because of %s",
				ex.getMessage().split("\"")[1], ex.getResourceId(), ex.getResourceType());

		DiscountErrorResponse errorResp = new DiscountErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), null,
				"Business Rule Failure", errorMessage, request.getRequestURI());

		return new ResponseEntity<DiscountErrorResponse>(errorResp, overrideContentType(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * fall-back handler – a catch-all type of logic that deals with all other
	 * exceptions that don’t have specific handlers.
	 *
	 * @param ex{@link
	 * 			Exception}
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<DiscountErrorResponse> handleAll(Exception ex, HttpServletRequest request) {

		LOGGER.error("An unexpected error occurred", ex);

		DiscountErrorResponse errorResp = new DiscountErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), null,
				"Internal Error", "An unexpected error has occurred", request.getRequestURI());

		return new ResponseEntity<DiscountErrorResponse>(errorResp, overrideContentType(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private HttpHeaders overrideContentType() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, APPLICATION_PROBLEM_JSON);
		return httpHeaders;
	}
}
