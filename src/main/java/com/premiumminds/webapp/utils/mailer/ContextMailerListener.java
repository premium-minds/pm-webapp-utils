package com.premiumminds.webapp.utils.mailer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.premiumminds.webapp.utils.WebAppFileLoader;

/**
 * Class to provide configurations for the Mailer class
 * this uses the context parameter "mailer-config"
 * 
 * This uses the WebAppFileLoader
 * 
 * @author acamilo
 *
 */
public class ContextMailerListener implements ServletContextListener {
	private static final Logger log = Logger.getLogger(ContextMailerListener.class);

	public void contextInitialized(ServletContextEvent sce) {
	    ServletContext context = sce.getServletContext( );

	    String url = context.getInitParameter("mailer-config");
	    
	    WebAppFileLoader fileLoader = new WebAppFileLoader(url, sce.getServletContext( ));

	    try {
			Mailer.configure(fileLoader.load());
			AbstractMailer.configure(fileLoader.load());
		} catch (Exception e) {
			log.fatal("couldn't load the Mailer properties on '"+url+"'");
			log.error("error configuring mailer", e);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
