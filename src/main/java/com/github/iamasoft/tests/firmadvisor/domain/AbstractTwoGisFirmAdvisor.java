package com.github.iamasoft.tests.firmadvisor.domain;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.github.iamasoft.tests.firmadvisor.ApplicationException;
import com.github.iamasoft.tests.firmadvisor.twogis.TwoGisClient;

/**
 * Abstract implementation of {@link IFirmAdvisor} interface that searches firms in 2GIS.
 *
 * @author Kirill V. Karavaev
 */
public abstract class AbstractTwoGisFirmAdvisor implements IFirmAdvisor {

	/** 2GIS API access key. */
	@Nonnull
	private final String apiKey;

	/**
	 * Constructs 2GIS firm advisor.
	 * @param apiKey
	 *        2GIS API access key
	 */
	public AbstractTwoGisFirmAdvisor(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Created an instance of {@link TwoGisClient} and delegates to {@link #advise(TwoGisClient, String, Collection)}.
	 */
	@Override
	public final List<AdvisedFirm> advise(String category, Collection<String> cities) throws ApplicationException {
		try (TwoGisClient twoGisClient = new TwoGisClient(apiKey)) {
			List<AdvisedFirm> firms = advise(twoGisClient, category, cities);
			// Sorting by rating in descending order
			firms.sort((one, two) -> two.getFlampRating().compareTo(one.getFlampRating()));
			// Ready
			return firms;
		}
	}

	/**
	 * Finds a list of the most popular firms in a given category using the given instance of {@link TwoGisClient}.
	 * The list don't have to be sorted.
	 */
	@ParametersAreNonnullByDefault
	protected abstract List<AdvisedFirm> advise(TwoGisClient twoGisClient, String category, Collection<String> cities)
			throws ApplicationException;

}
