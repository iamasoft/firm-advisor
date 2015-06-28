package com.github.iamasoft.jobs.firmadvisor;

import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;

/**
 * Firm Advisor web API root resource.
 *
 * @author Kirill V. Karavaev
 */
@Path("/")
public class ApiRootResource {

	/** Media type: JSON in UTF-8 encoding. Use it when non-Latin characters expected. */
	public static final String APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON + ";charset=UTF-8";

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@JacksonFeatures(serializationEnable = { SerializationFeature.INDENT_OUTPUT })
	public Object get() {
		return Arrays.asList("Жил", "без", "страха", "и", "умер", "без", "страха");
	}

}
