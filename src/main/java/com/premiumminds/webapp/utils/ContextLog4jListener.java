package com.premiumminds.webapp.utils;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

/**
 * Class to load Log4j configurations at the servlet context
 * the context parameter "logger-config" is used
 * 
 * @author acamilo
 *
 */
public class ContextLog4jListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
	    ServletContext context = sce.getServletContext( );
	    
	    String url = context.getInitParameter("logger-config");
	    
	    WebAppFileLoader fileLoader = new WebAppFileLoader(url, sce.getServletContext( ));

	    Properties properties = new Properties();
	    try {
			properties.load(fileLoader.load());
		    PropertyConfigurator.configure(properties);
		} catch (Exception e) {
			System.err.println("Couldn't load the log4j properties file on '"+url+"'");
			e.printStackTrace();
		}
	    
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
