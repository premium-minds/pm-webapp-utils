package com.premiumminds.webapp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class for the application configurations at servlet level
 * With this you can have different configurations depending on the web.xml
 * 
 * This is a singleton, so you can access it anywhere on the code
 * 
 * @author acamilo
 *
 */
public class ApplicationContextConfiguration extends Properties {
	private static final long serialVersionUID = 8991242938128918789L;

	private static ApplicationContextConfiguration instance;
	
	/**
	 * Gets the configuration for the application on the context
	 * 
	 * Throws an RuntimeException if the configuration wasn't loaded
	 * 
	 * @return the application properties
	 */
	public static ApplicationContextConfiguration get(){
		if(instance==null) throw new RuntimeException("Application configuration was not set...");
		return instance;
	}

	private ApplicationContextConfiguration() {
		super();
	}
	
	/**
	 * Loads the configuration to the context
	 * 
	 * @param is InputStream with the configuration
	 */
	public static void configure(InputStream is){
		if(instance==null) instance = new ApplicationContextConfiguration();
		try {
			instance.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
