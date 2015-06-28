package com.github.iamasoft.tests.firmadvisor.domain;

import java.util.Collection;
import java.util.List;

import com.github.iamasoft.tests.firmadvisor.ApplicationException;

/**
 * An interface of a Firm Advisor service, which find the most popular firms.
 *
 * @author Kirill V. Karavaev
 */
public interface IFirmAdvisor {

	/**
	 * Returns a list of the most popular firms in a given category. The filials searched in the given cities.
	 * @param category
	 *        the category of the filials to be found, e.g. "кинотеатр", "столовая", etc.
	 * @param cities
	 *        the names of the cities where to find the filials
	 * @return the list of advised filial descriptors
	 * @throws ApplicationException
	 *         in case of error while getting the firms
	 */
	List<AdvisedFirm> advise(String category, Collection<String> cities) throws ApplicationException;

}
