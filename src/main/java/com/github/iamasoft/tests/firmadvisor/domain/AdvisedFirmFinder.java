package com.github.iamasoft.tests.firmadvisor.domain;

import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.github.iamasoft.tests.firmadvisor.ApplicationException;
import com.github.iamasoft.tests.firmadvisor.twogis.FirmFilial;
import com.github.iamasoft.tests.firmadvisor.twogis.FirmFilialProfileResponse;
import com.github.iamasoft.tests.firmadvisor.twogis.TwoGisClient;

/**
 * This class can be used to find a single {@link AdvisedFirm}.
 *
 * @author Kirill V. Karavaev
 */
public class AdvisedFirmFinder implements Callable<AdvisedFirm> {

	/** Instance of 2GIS API client to be used to find the firm. */
	@Nonnull
	private final TwoGisClient twoGisClient;

	/** Name of a city where to search for the firm. */
	@Nonnull
	private final String city;

	/** Category of the firm to be found. */
	@Nonnull
	private final String category;

	/**
	 * Constructs new firm finder.
	 * @param twoGisClient
	 *        an instance of {@link TwoGisClient}
	 * @param city
	 *        a city where to search for a firm
	 * @param category
	 *        a category of a firm to be found
	 */
	@ParametersAreNonnullByDefault
	public AdvisedFirmFinder(TwoGisClient twoGisClient, String city, String category) {
		this.twoGisClient = twoGisClient;
		this.city = city;
		this.category = category;
	}

	/**
	 * Attempts to find a firm in the city.
	 * @return most popular firm in the city by category, or {@code null} if such firm does not exist
	 * @throws ApplicationException
	 *         if the 2GIS API client invocation failed
	 */
	@Override
	public AdvisedFirm call() throws ApplicationException {
		// Looking for a most popular filial
		FirmFilial filial = twoGisClient.findMostPopularFilial(city, category);
		if (filial != null) {
			// Filial found, retrieving profile
			FirmFilialProfileResponse filialProfile = twoGisClient.getFilialProfile(filial);
			Double flampRating = filialProfile.getFirmFlampRating();
			if (flampRating != null && flampRating != 0.0) {
				return new AdvisedFirm(filial.getName(),
						filial.getCity() + ", " + filial.getAddress(),
						flampRating);
			} else {
				// We are not interested in the firms without Flamp rating
				return null;
			}
		} else {
			// Nothing found in the city
			return null;
		}
	}

}
