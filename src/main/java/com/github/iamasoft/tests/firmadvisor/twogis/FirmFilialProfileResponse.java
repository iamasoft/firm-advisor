package com.github.iamasoft.tests.firmadvisor.twogis;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This class represents a profile of a firm filial in 2GIS catalog.
 *
 * @author Kirill V. Karavaev
 */
@NotThreadSafe
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirmFilialProfileResponse extends AbstractTwoGisResponse {

	/** Rating on {@code Flamp.ru} of the firm this filial belongs to. */
	@Nullable
	private Double firmFlampRating;

	/**
	 * Constructs a response.
	 * @param apiVersion
	 *        2GIS API version
	 * @param responseCode
	 *        response code, indicates a status of a corresponding request
	 */
	@JsonCreator
	public FirmFilialProfileResponse(@JsonProperty(PARAM_API_VERSION) String apiVersion,
			@JsonProperty(PARAM_RESPONSE_CODE) String responseCode) {
		super(apiVersion, responseCode);
	}

	/**
	 * @return the rating on {@code Flamp.ru} of the firm this filial belongs to; {@code null} indicates no rating
	 */
	@CheckForNull
	public Double getFirmFlampRating() {
		return firmFlampRating;
	}

	/**
	 * Sets a Flamp rating of the firm this filial belongs to.
	 * @param firmFlampRating
	 *        the value to be set
	 */
	@JsonSetter("rating")
	public void setFirmFlampRating(@Nullable Double firmFlampRating) {
		this.firmFlampRating = firmFlampRating;
	}

}
