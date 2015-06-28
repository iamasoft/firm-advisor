package com.github.iamasoft.tests.firmadvisor.domain;

import java.util.LinkedList;

/**
 * A list that contains name of the cities where to find advised firms. Usable for injection.
 *
 * @author Kirill V. Karavaev
 */
public class CityNamesList extends LinkedList<String>{

	private static final long serialVersionUID = -3374434774092545443L;

	/**
	 * Constructs a list that contains given city names.
	 * @param cityNames
	 *        the names to be added to the list
	 */
	public CityNamesList(String... cityNames) {
		super();
		for (String cityName : cityNames) {
			add(cityName);
		}
	}

}
