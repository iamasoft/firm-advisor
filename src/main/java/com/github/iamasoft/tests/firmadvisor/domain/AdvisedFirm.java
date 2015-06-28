package com.github.iamasoft.tests.firmadvisor.domain;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * This class represents information about a firm advised by {@link IFirmAdvisor} service.
 *
 * @author Kirill V. Karavaev
 */
public class AdvisedFirm {

	/** Name of the firm. */
	@Nonnull
	private final String name;

	/** Firm physical address. */
	@Nonnull
	private final String address;

	/** Rating of the firm on Flamp. */
	@Nonnull
	private final Double flampRating;

	/**
	 * Constructs new advised firm object.
	 * @param name
	 *        firm name
	 * @param address
	 *        firm address
	 * @param flampRating
	 *        firm rating
	 */
	@ParametersAreNonnullByDefault
	public AdvisedFirm(String name, String address, Double flampRating) {
		this.name = name;
		this.address = address;
		this.flampRating = flampRating;
	}

	/**
	 * @return the name of the firm
	 */
	@JsonGetter
	@Nonnull
	public String getName() {
		return name;
	}

	/**
	 * @return the firm address
	 */
	@JsonGetter
	@Nonnull
	public String getAddress() {
		return address;
	}

	/**
	 * @return the rating of the firm on Flamp
	 */
	@JsonGetter
	@Nonnull
	public Double getFlampRating() {
		return flampRating;
	}

}
