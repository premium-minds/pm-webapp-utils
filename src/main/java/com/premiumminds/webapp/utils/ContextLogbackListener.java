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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class ContextLogbackListener implements ServletContextListener {
	/**
	 * Class to load Log4j configurations at the servlet context the context
	 * parameter "logger-config" is used
	 * 
	 * @author acamilo
	 * 
	 */

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		String url = context.getInitParameter("logger-config");

		if(url==null) throw new RuntimeException("couldn't get webapp parameter 'logger-config'");
		WebAppFileLoader fileLoader = new WebAppFileLoader(url,
				sce.getServletContext());

		LoggerContext logContext = (LoggerContext) LoggerFactory.getILoggerFactory();  
		try {  
		  logContext.reset();
		  JoranConfigurator configurator = new JoranConfigurator();  
		  configurator.setContext(logContext);  
		  configurator.doConfigure(fileLoader.load()); // loads logback file  
		} catch (JoranException ex) {  
		  ex.printStackTrace(); // Just in case, so we see a stacktrace  
		} catch (Exception ex) {  
		  ex.printStackTrace(); // Just in case, so we see a stacktrace  
		}  
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
