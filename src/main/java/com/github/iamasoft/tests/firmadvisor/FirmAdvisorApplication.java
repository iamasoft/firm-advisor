package com.github.iamasoft.tests.firmadvisor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.iamasoft.tests.firmadvisor.domain.CityNamesList;
import com.github.iamasoft.tests.firmadvisor.domain.IFirmAdvisor;
import com.github.iamasoft.tests.firmadvisor.domain.ParallelTwoGisFirmAdvisor;

/**
 * Web application entry point.
 *
 * @author Kirill V. Karavaev
 */
public class FirmAdvisorApplication extends ResourceConfig {

	/** Name of a file that contains application properties. */
	private static final String PROPERTIES_FILE = "properties.xml";

	/** Name of a property that contains 2GIS API key. */
	private static final String PROPERTY_API_KEY = "2gis.api.key";

	/** Name of a property that contains comma-separated list of the cities where to find advised firms. */
	private static final String PROPERTY_CITIES = "search.in.cities";

	/**
	 * Constructs application instance.
	 * @throws ApplicationException
	 */
	public FirmAdvisorApplication() throws ApplicationException {
		Properties applicationProperties = initializeProperties();
		// Registering services
		register(JacksonJsonProvider.class);
		register(DefaultExceptionMapper.class);
		register(new InjectionsBinder(applicationProperties));
		// Registering WEB services
		registerResources(Resource.from(ApiRootResource.class));
	}

	/**
	 * Loads application properties from a classpath resource.
	 * @return the application {@link Properties properties} container
	 * @throws ApplicationException
	 *         if the properties file cannot be read
	 */
	private Properties initializeProperties() throws ApplicationException {
		// Locating classpath resource
		URL propertiesFile = FirmAdvisorApplication.class.getResource("/" + PROPERTIES_FILE);
		if (propertiesFile == null) {
			throw new ApplicationException("Unable to find properties file " + PROPERTIES_FILE);
		}
		// Reading properties file
		Properties properties = new Properties();
		try (InputStream stream = propertiesFile.openStream()) {
			properties.loadFromXML(stream);
		} catch (IOException e) {
			throw new ApplicationException("Unable to read properties file", e);
		}
		return properties;
	}

	/**
	 * Binder of the injectable internal services.
	 */
	private static final class InjectionsBinder extends AbstractBinder {

		@Nonnull
		private final Properties properties;

		private InjectionsBinder(Properties properties) {
			this.properties = properties;
		}

		@Override
		protected void configure() {
			// 2GIS Client instance
			String apiKey = properties.getProperty(PROPERTY_API_KEY, "API key not provided");
			bind(new ParallelTwoGisFirmAdvisor(apiKey)).to(IFirmAdvisor.class);
			// List of the cities
			String cities = properties.getProperty(PROPERTY_CITIES, "");
			bind(new CityNamesList(cities.split("\\s*,\\s*")));
		}
	}

}
