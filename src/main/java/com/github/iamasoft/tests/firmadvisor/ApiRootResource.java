package com.github.iamasoft.tests.firmadvisor;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.github.iamasoft.tests.firmadvisor.domain.AdvisedFirm;
import com.github.iamasoft.tests.firmadvisor.domain.CityNamesList;
import com.github.iamasoft.tests.firmadvisor.domain.IFirmAdvisor;

/**
 * Firm Advisor web API root resource.
 *
 * @author Kirill V. Karavaev
 */
@Path("/api")
public class ApiRootResource {

	/** Media type: JSON in UTF-8 encoding. Use it when non-Latin characters expected. */
	public static final String APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON + ";charset=UTF-8";

	/** Injected instance of {@link IFirmAdvisor}. */
	@Nonnull
	private final IFirmAdvisor firmAdvisor;

	/** Injected list of the cities where to find the firms. */
	@Nonnull
	private final CityNamesList cities;

	/**
	 * Constructs resource instance. Called by Jersey.
	 * @param firmAdvisor
	 *        the instance {@link IFirmAdvisor} to be used to advice the firms
	 * @param cities
	 *        the list of the cities where to find the firms
	 */
	@Inject
	@ParametersAreNonnullByDefault
	public ApiRootResource(IFirmAdvisor firmAdvisor, CityNamesList cities) {
		this.firmAdvisor = firmAdvisor;
		this.cities = cities;
	}

	/**
	 * Finds the most popular firms of a given category in the pre-configured cities.
	 * @param category
	 *        the category of the firms to be found
	 * @return the collection of {@link AdvisedFirm} ordered by their Flamp rating
	 * @throws ApplicationException if the firm advisor invocation failed
	 */
	@GET
	@Path("firms")
	@Produces(APPLICATION_JSON_UTF8)
	@JacksonFeatures(serializationEnable = { SerializationFeature.INDENT_OUTPUT })
	public Collection<AdvisedFirm> adviceFirms(@QueryParam("category") String category)
			throws ApplicationException {
		return firmAdvisor.advise(category, cities);
	}

}
