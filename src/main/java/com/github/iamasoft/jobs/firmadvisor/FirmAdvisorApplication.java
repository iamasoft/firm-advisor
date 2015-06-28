package com.github.iamasoft.jobs.firmadvisor;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * Web application entry point.
 *
 * @author Kirill V. Karavaev
 */
public class FirmAdvisorApplication extends ResourceConfig {

	/**
	 * Constructs application instance.
	 */
	public FirmAdvisorApplication() {
		register(JacksonJsonProvider.class);
		registerResources(Resource.from(ApiRootResource.class));
	}

}
