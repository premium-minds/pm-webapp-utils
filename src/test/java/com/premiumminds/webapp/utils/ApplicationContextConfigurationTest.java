package com.premiumminds.webapp.utils;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.jetty.testing.ServletTester;

import junit.framework.TestCase;

public class ApplicationContextConfigurationTest extends TestCase {
	private ServletTester tester;
	
	@Override
	protected void setUp() throws Exception {
		tester = new ServletTester();
		
		URL url = this.getClass().getResource("/");
		tester.setResourceBase(url.getPath());
	}
	
	@Override
	protected void tearDown() throws Exception {
	}
	
	public void testSuccessLoad(){
		tester.addEventListener(new ApplicationContextConfigurationListener());
		tester.getContext().setInitParameter("app-config", "classpath:/com/premiumminds/webapp/utils/applicationConfigurationTest.properties");
		try {
			tester.start();
			assertTrue(ApplicationContextConfiguration.get().containsKey("application"));
			assertTrue("test".equals(ApplicationContextConfiguration.get().getProperty("application")));
			
	    	WebAppFileLoader loader = new WebAppFileLoader("classpath:/com/premiumminds/webapp/utils/applicationConfigurationTest2.properties");
	    	InputStream stream = loader.load();
	    	ApplicationContextConfiguration.configure(stream);
			stream.close();

			assertTrue("test2".equals(ApplicationContextConfiguration.get().getProperty("application")));

			tester.stop();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
