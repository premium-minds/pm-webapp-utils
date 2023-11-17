/**
 * Copyright (C) 2014 Premium Minds.
 *
 * This file is part of pm-webapp-utils.
 *
 * pm-webapp-utils is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * pm-webapp-utils is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with pm-webapp-utils. If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.webapp.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.jupiter.api.Test;

public class ApplicationContextConfigurationTest {

	@Test
	public void testSuccessLoad() throws Exception {
		Server server = new Server();
		server.addConnector(new LocalConnector(server));
		ServletContextHandler context = new ServletContextHandler(server, "/contextPath");
		context.addEventListener(new ApplicationContextConfigurationListener());
		context.setInitParameter("app-config", "classpath:/com/premiumminds/webapp/utils/applicationConfigurationTest.properties");

		server.start();

		assertTrue(ApplicationContextConfiguration.get().containsKey("application"));
		assertEquals("test", ApplicationContextConfiguration.get().getProperty("application"));

		WebAppFileLoader loader = new WebAppFileLoader("classpath:/com/premiumminds/webapp/utils/applicationConfigurationTest2.properties");
		try (InputStream stream = loader.load()) {
			ApplicationContextConfiguration.configure(stream);
		}

		assertEquals("test2", ApplicationContextConfiguration.get().getProperty("application"));
		assertEquals("123", ApplicationContextConfiguration.get().getProperty("application2"));

		server.stop();
	}
}
