package com.github.iamasoft.tests.firmadvisor.twogis;

import java.io.Closeable;
import java.util.Objects;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.iamasoft.tests.firmadvisor.ApplicationException;

/**
 * This class provides access to 2GIS API in a manner required for Firm Advisor application.
 *
 * @author Kirill V. Karavaev
 */
@ThreadSafe
public final class TwoGisClient implements Closeable {

	/** Version of the 2GIS API used by this client. */
	private static final String API_VERSION = "1.3";

	/** 2GIS Catalog API endpoint URI. */
	private static final String CATALOG_API_ENDPOINT = "http://catalog.api.2gis.ru";

	/** 2GIS Catalog API: Firm Search request endpoint URI. */
	private static final String FIRM_SEARCH_ENDPOINT = CATALOG_API_ENDPOINT + "/search";

	/** 2GIS Catalog API: Firm Profile request endpoint URI. */
	private static final String FIRM_PROFILE_ENDPOINT = CATALOG_API_ENDPOINT + "/profile";

	/** HTTP client instance. */
	private final Client httpClient;

	/** 2GIS API key. */
	private final String apiKey;

	/**
	 * Constructs 2GIS API Client.
	 * @param apiKey
	 *        API access key
	 */
	public TwoGisClient(String apiKey) {
		this.httpClient = ClientBuilder.newBuilder().register(JacksonJsonProvider.class).build();
		this.apiKey = apiKey;
	}

	/**
	 * Finds a most popular (by Flamp rating) firm filial an a city.
	 * @param city
	 *        name of a city where to find the filial
	 * @param category
	 *        category the filial belongs to, e.g. "кинотеатр", "столовая", etc.
	 * @return {@link FirmFilial} object of the filial found, {@code null} otherwise
	 * @throws ApplicationException
	 *         if request to 2GIS API failed
	 */
	@CheckForNull
	@ParametersAreNonnullByDefault
	public FirmFilial findMostPopularFilial(String city, String category) throws ApplicationException {
		// Searching filials sorted by Flamp rating
		FirmSearchResponse response = makeQueryTarget(FIRM_SEARCH_ENDPOINT)
				.queryParam("where", city)
				.queryParam("what", category)
				.queryParam("sort", "rating") // Sorting by Flamp rating
				.queryParam("pagesize", 5) // Minimal allowed value is 5, though we want only one
				.request(MediaType.APPLICATION_JSON_TYPE)
				.get(FirmSearchResponse.class);
		// Processing response
		if (AbstractTwoGisResponse.CODE_NOT_FOUND.equals(response.getResponseCode())) {
			// Nothing found for a given criteria
			return null;
		}
		throwExceptionIfError(response);
		// Success
		if (!response.getResult().isEmpty()) {
			// Firm filial found
			return response.getResult().iterator().next();
		} else {
			// Nothing found
			return null;
		}
	}

	/**
	 * Retrieves a profile of a given firm filial.
	 * @param filial
	 *        the filial information holder
	 * @return {@link FirmFilialProfileResponse} object of the firm filial
	 * @throws ApplicationException
	 *         if request to 2GIS API failed
	 */
	@Nonnull
	public FirmFilialProfileResponse getFilialProfile(@Nonnull FirmFilial filial) throws ApplicationException {
		// Retrieving filial profile
		FirmFilialProfileResponse response = makeQueryTarget(FIRM_PROFILE_ENDPOINT)
				.queryParam("id", filial.getId())
				.queryParam("hash", filial.getHash())
				.request(MediaType.APPLICATION_JSON_TYPE)
				.get(FirmFilialProfileResponse.class);
		// Processing response
		throwExceptionIfError(response);
		// Success
		return response;
	}

	/**
	 * Closes an underlying HTTP client instance and all associated resources. Subsequent calls have no effect and are
	 * ignored. Once the client is closed, invoking any other method on the client instance would result in an
	 * {@link IllegalStateException} being thrown.
	 */
	@Override
	public void close() {
		httpClient.close();
	}

	/**
	 * Creates {@link WebTarget} for a given endpoint.
	 * @param endpoint
	 *        the endpoint URI
	 * @return {@link WebTarget} instance pre-configured with API version and key parameters
	 */
	private WebTarget makeQueryTarget(String endpoint) {
		return httpClient.target(endpoint)
				.queryParam("version", API_VERSION)
				.queryParam("key", apiKey);
	}

	/**
	 * Throws {@link WebApplicationException} if a given response is not successful.
	 * @param response
	 *        the response to be checked
	 * @throws WebApplicationException
	 *         if the given response is not successful
	 */
	private static void throwExceptionIfError(AbstractTwoGisResponse response) throws ApplicationException {
		if (!response.isSuccess()) {
			// Request failed, analyzing the reason
			String responseCode = Objects.toString(response.getResponseCode(), "");
			String errorDetails = makeErrorDetails(response);
			switch (responseCode) {
				case AbstractTwoGisResponse.CODE_BAD_REQUEST:
				case AbstractTwoGisResponse.CODE_FORBIDDEN:
				case AbstractTwoGisResponse.CODE_NOT_FOUND:
				case AbstractTwoGisResponse.CODE_SERVER_ERROR:
				case AbstractTwoGisResponse.CODE_SERVICE_UNAVAILABLE:
					throw new ApplicationException(errorDetails);
				default:
					throw new ApplicationException("Unexpected response code: " + responseCode);
			}
		}
	}

	/**
	 * Constructs an error details string for a given failed response.
	 * @param response
	 *        the response containing information about error
	 * @return the string that describes the error
	 */
	@Nonnull
	private static String makeErrorDetails(@Nonnull AbstractTwoGisResponse response) {
		final String preamble = "Request to 2GIS API failed: ";
		String errorMessage = response.getErrorMessage();
		String errorCode = response.getErrorCode();
		// Constructing error details string
		if (errorMessage != null && !errorMessage.isEmpty()) {
			// Error message provided by 2GIS service
			return preamble + errorMessage;
		} else if (errorCode != null && !errorCode.isEmpty()) {
			// No message, but got an error code
			return preamble + "error code is '" + errorCode + "'";
		} else {
			// No information about error
			return preamble + "no details provided";
		}
	}

}
