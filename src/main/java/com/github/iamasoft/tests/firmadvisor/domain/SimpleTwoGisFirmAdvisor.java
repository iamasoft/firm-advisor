package com.github.iamasoft.tests.firmadvisor.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.iamasoft.tests.firmadvisor.ApplicationException;
import com.github.iamasoft.tests.firmadvisor.twogis.TwoGisClient;

/**
 * Implementation of a {@link IFirmAdvisor} that sequentially finds {@link AdvisedFirm}s in every given city.
 *
 * @author Kirill V. Karavaev
 */
public class SimpleTwoGisFirmAdvisor extends AbstractTwoGisFirmAdvisor {

	/**
	 * Constructs 2GIS firm advisor.
	 * @param apiKey
	 *        2GIS API access key
	 */
	public SimpleTwoGisFirmAdvisor(String apiKey) {
		super(apiKey);
	}

	/**
	 * Invokes {@link AdvisedFirmFinder} and collects the results in sequence.
	 * @throws ApplicationException
	 *         if any of the finder invocation fails
	 */
	@Override
	protected List<AdvisedFirm> advise(TwoGisClient twoGisClient, String category, Collection<String> cities)
			throws ApplicationException {
		List<AdvisedFirm> firms = new ArrayList<>(cities.size());
		for (String city : cities) {
			AdvisedFirm firm = new AdvisedFirmFinder(twoGisClient, city, category).call();
			if (firm != null) {
				// Not interested in nulls
				firms.add(firm);
			}
		}
		// Ready
		return firms;
	}

}
