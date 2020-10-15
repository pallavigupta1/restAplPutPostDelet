package com.rc.api;



import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.Session;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class ApiApplication extends ResourceConfig {
	
	public ApiApplication() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
		provider.setMapper(mapper);
		register(provider);
		try(Session session = SessionUtil.getSession()){}
	}
}
