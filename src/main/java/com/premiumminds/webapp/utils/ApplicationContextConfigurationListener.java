package com.premiumminds.webapp.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/**
 * Class to load the Application configurations at the servlet context
 * the context parameter used is "app-config"
 * 
 * @author acamilo
 *
 */
public class ApplicationContextConfigurationListener implements ServletContextListener {
	private static final Logger log = Logger.getLogger(ApplicationContextConfigurationListener.class);

	public void contextInitialized(ServletContextEvent sce) {
	    ServletContext context = sce.getServletContext( );

	    String url = context.getInitParameter("app-config");
	    
	    WebAppFileLoader fileLoader = new WebAppFileLoader(url, sce.getServletContext( ));
	    
	    try {
			ApplicationContextConfiguration.configure(fileLoader.load());
		} catch (Exception e) {
			log.fatal("couldn't load the properties file '"+url+"'");
			log.error("error loading application properties", e);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
