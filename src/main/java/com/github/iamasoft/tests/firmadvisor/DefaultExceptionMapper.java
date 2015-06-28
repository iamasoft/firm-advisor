package com.github.iamasoft.tests.firmadvisor;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Service that maps exceptions occurred in Firm Advisor WEB application to a JSON response.
 *
 * @author Kirill V. Karavaev
 */
@Produces(MediaType.APPLICATION_JSON)
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {

	/**
	 * Creates response that contains {@link ErrorResponse} that contains an exception detail string.
	 */
	@Override
	public Response toResponse(Exception exception) {
		StatusType status;
		String errorDetails;
		// Retrieving error details
		if (exception instanceof WebApplicationException) {
			// Standard web service exception
			WebApplicationException wae = (WebApplicationException) exception;
			status = wae.getResponse().getStatusInfo();
			errorDetails = wae.toString();
		} else {
			// Other exceptions
			status = Status.INTERNAL_SERVER_ERROR;
			Throwable cause = exception.getCause();
			errorDetails = exception.toString();
			if (cause != null) {
				errorDetails += ", caused by " + cause.toString();
			}
		}
		// Generating response
		return Response.status(status)
				.entity(new ErrorResponse(status.getStatusCode(), errorDetails))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

	/**
	 * Response entity that contains error details.
	 */
	public static final class ErrorResponse {

		/** HTTP status code of the error. */
		private final int statusCode;

		/** Error details string. */
		@Nonnull
		private final String details;

		@ParametersAreNonnullByDefault
		private ErrorResponse(int statusCode, String details) {
			this.statusCode = statusCode;
			this.details = details;
		}

		/**
		 * @return the HTTP status code of the error
		 */
		public int getStatusCode() {
			return statusCode;
		}

		/**
		 * @return the error details string
		 */
		@Nonnull
		public String getErrorDetails() {
			return details;
		}
	}

}
