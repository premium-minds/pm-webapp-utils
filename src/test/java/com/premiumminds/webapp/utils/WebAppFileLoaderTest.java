package com.premiumminds.webapp.utils;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jetty.testing.ServletTester;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WebAppFileLoaderTest extends TestCase {
	public static Test suite(){
		return new TestSuite(WebAppFileLoaderTest.class);
	}
	
	public void testFilenotfound(){
		String[] filenames = 
			{
				"filesystem:/path/to/file.txt", 
				"filesystem:/file.txt",
				"classpath:/com/premiumminds/webapp/utils/TestFile.txt"
			};
		
		for(String filename : filenames){
			WebAppFileLoader fileLoader = new WebAppFileLoader(filename);
			try {
				fileLoader.load();
				fail("should throw FileNotFound");
			} catch (URISyntaxException e) {
				fail(e.getMessage());
			} catch (FileNotFoundException e) {
			}
		}
		WebAppFileLoader fileLoader = new WebAppFileLoader("context:/file.txt");
		try {
			fileLoader.load();
			fail("should throw RuntimeException because there is no context");
		} catch(RuntimeException e){
		} catch (FileNotFoundException e) {
			fail("should throw RuntimeException because there is no context");
		} catch (URISyntaxException e) {
			fail("should throw RuntimeException because there is no context");
		}
	}
	
	public void testSuccess(){
		WebAppFileLoader fileLoader = new WebAppFileLoader("classpath:/com/premiumminds/webapp/utils/teste.txt");
		loadSuccess(fileLoader);
		
		URL url = this.getClass().getResource("/com/premiumminds/webapp/utils/teste.txt");
		fileLoader = new WebAppFileLoader("filesystem:"+url.getPath());
		loadSuccess(fileLoader);
	}

	private void loadSuccess(WebAppFileLoader fileLoader) {
		try {
			fileLoader.load();
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}
	}
	
	public void testWithServletContext(){
		ServletTester tester = new ServletTester();
		
		URL url = this.getClass().getResource("/");
		tester.setResourceBase(url.getPath());
		WebAppFileLoader fileloader = new WebAppFileLoader("context:/com/premiumminds/webapp/utils/teste.txt", tester.getContext().getServletContext());
		loadSuccess(fileloader);
	}
}
