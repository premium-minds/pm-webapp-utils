package com.premiumminds.webapp.utils.mailer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger log = LoggerFactory.getLogger(ContextMailerListener.class);

	public void contextInitialized(ServletContextEvent sce) {
	    ServletContext context = sce.getServletContext( );

	    String url = context.getInitParameter("mailer-config");
	    
	    WebAppFileLoader fileLoader = new WebAppFileLoader(url, sce.getServletContext( ));

	    try {
			Mailer.configure(fileLoader.load());
			AbstractMailer.configure(fileLoader.load());
		} catch (Exception e) {
			log.error("couldn't load the Mailer properties on '"+url+"'", e);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
