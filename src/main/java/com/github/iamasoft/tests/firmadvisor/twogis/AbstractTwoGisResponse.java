package com.github.iamasoft.tests.firmadvisor.twogis;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Abstract class for 2GIS API call responses. Contains request status and respective details.
 *
 * @author Kirill V. Karavaev
 * @see <a href="http://api.2gis.ru/doc/firms/response-codes#responses">2GIS API Response Codes</a>
 */
public abstract class AbstractTwoGisResponse {

	/** Indicates successful request. */
	public static final String CODE_SUCCESS = "200";

	/** Indicates errors in request. */
	public static final String CODE_BAD_REQUEST = "400";

	/** Indicates that access to requested resource is not allowed. */
	public static final String CODE_FORBIDDEN = "403";

	/** Indicates that requested resource does not exist. */
	public static final String CODE_NOT_FOUND = "404";

	/** Indicates internal server error. */
	public static final String CODE_SERVER_ERROR = "500";

	/** Indicates that requested service is temporarily unavailable. */
	public static final String CODE_SERVICE_UNAVAILABLE = "503";

	/** Name of JSON attribute that contains a value for {@link #apiVersion}. */
	protected static final String PARAM_API_VERSION = "api_version";

	/** Name of JSON attribute that contains a value for {@link #responseCode}. */
	protected static final String PARAM_RESPONSE_CODE = "response_code";

	/** Version of the 2GIS API. */
	@Nonnull
	private final String apiVersion;

	/** Response code. */
	@Nonnull
	private final String responseCode;

	/** Error code. */
	@Nullable
	private String errorCode;

	/** Error message. */
	@Nullable
	private String errorMessage;

	/**
	 * Constructs a response.
	 * @param apiVersion
	 *        2GIS API version
	 * @param responseCode
	 *        response code, indicates a status of a corresponding request
	 */
	@JsonCreator
	public AbstractTwoGisResponse(@JsonProperty(PARAM_API_VERSION) String apiVersion,
			@JsonProperty(PARAM_RESPONSE_CODE) String responseCode) {
		super();
		this.apiVersion = apiVersion;
		this.responseCode = responseCode;
	}

	/**
	 * @return {@code true} if the request was successful, {@false} otherwise (see error details in
	 *         {@link #getErrorCode()} and {@link #getErrorMessage()})
	 */
	public boolean isSuccess() {
		return CODE_SUCCESS.equals(responseCode);
	}

	/**
	 * @return the version of the 2GIS API
	 */
	@Nonnull
	public final String getApiVersion() {
		return apiVersion;
	}

	/**
	 * @return the response code
	 */
	@Nonnull
	public final String getResponseCode() {
		return responseCode;
	}

	/**
	 * @return the error code in case the request was not successful; {@code null} otherwise
	 */
	@CheckForNull
	public final String getErrorCode() {
		return errorCode;
	}

	/**
	 * Sets a code of error in case the request was not successful.
	 * @param errorCode
	 *        the value to set
	 */
	@JsonSetter("error_code")
	public final void setErrorCode(@Nullable String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the error message in case the request was not successful; {@code null} otherwise
	 */
	@CheckForNull
	public final String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets a message that describes the error.
	 * @param errorMessage
	 *        the value to set
	 */
	@JsonSetter("error_message")
	public final void setErrorMessage(@Nullable String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
