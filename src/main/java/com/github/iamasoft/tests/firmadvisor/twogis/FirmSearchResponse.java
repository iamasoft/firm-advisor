package com.github.iamasoft.tests.firmadvisor.twogis;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This class represents a response from a Firm Search request to the 2GIS API.
 *
 * @author Kirill V. Karavaev
 */
@NotThreadSafe
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirmSearchResponse extends AbstractTwoGisResponse {

	/** List of filials found. */
	private final List<FirmFilial> filials;

	/**
	 * Constructs a response.
	 * @param apiVersion
	 *        2GIS API version
	 * @param responseCode
	 *        response code, indicates a status of a corresponding request
	 */
	@JsonCreator
	public FirmSearchResponse(@JsonProperty(PARAM_API_VERSION) String apiVersion,
			@JsonProperty(PARAM_RESPONSE_CODE) String responseCode) {
		super(apiVersion, responseCode);
		this.filials = new LinkedList<>();
	}

	/**
	 * Sets a list of firm filials found for a request.
	 * @param filials
	 *        the list of firm filials
	 */
	@JsonSetter("result")
	@JsonDeserialize(contentAs = FirmFilial.class)
	public final void setFilials(@Nonnull List<FirmFilial> filials) {
		this.filials.clear();
		this.filials.addAll(filials);
	}

	/**
	 * @return the unmodifiable list of firm filials found
	 */
	@Nonnull
	public final List<FirmFilial> getResult() {
		return Collections.unmodifiableList(filials);
	}

}
