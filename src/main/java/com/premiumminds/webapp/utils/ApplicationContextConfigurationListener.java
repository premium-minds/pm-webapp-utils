package com.premiumminds.webapp.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class to load the Application configurations at the servlet context
 * the context parameter used is "app-config"
 * 
 * @author acamilo
 *
 */
public class ApplicationContextConfigurationListener implements ServletContextListener {
	private static final Logger log = LoggerFactory.getLogger(ApplicationContextConfigurationListener.class);

	public void contextInitialized(ServletContextEvent sce) {
	    ServletContext context = sce.getServletContext( );

	    String url = context.getInitParameter("app-config");
	    
		if(url==null) throw new RuntimeException("couldn't get webapp parameter 'app-config'");
	    WebAppFileLoader fileLoader = new WebAppFileLoader(url, sce.getServletContext( ));
	    
	    try {
			ApplicationContextConfiguration.configure(fileLoader.load());
		} catch (Exception e) {
			log.error("couldn't load the properties file '"+url+"'");
			log.error("error loading application properties", e);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
