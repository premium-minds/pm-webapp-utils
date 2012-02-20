package com.premiumminds.webapp.utils;

import java.net.URISyntaxException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WebAppFileLoaderTest extends TestCase {
	public static Test suite(){
		return new TestSuite(WebAppFileLoaderTest.class);
	}
	
	public void testFilename(){
		String[] filenames = 
			{
				"filesystem:///path/to/file.txt", 
				"filesystem:///file.txt",
				"classpath://com.premiumminds.webapp.utils.TestFile.txt"
			};
		
		for(String filename : filenames){
			WebAppFileLoader fileLoader = new WebAppFileLoader(filename);
			try {
				fileLoader.load();
			} catch (URISyntaxException e) {
				fail(e.getMessage());
			}
		}
	}
}
