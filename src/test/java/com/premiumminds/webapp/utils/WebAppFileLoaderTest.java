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

import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jetty.servlet.ServletTester;
import org.junit.jupiter.api.Test;

public class WebAppFileLoaderTest {

	@Test
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
		} catch (FileNotFoundException | URISyntaxException e) {
			fail("should throw RuntimeException because there is no context");
		}
	}

	@Test
	public void testSuccess(){
		WebAppFileLoader fileLoader = new WebAppFileLoader("classpath:/com/premiumminds/webapp/utils/WebAppFileLoaderTestFile.txt");
		loadSuccess(fileLoader);
		
		URL url = this.getClass().getResource("/com/premiumminds/webapp/utils/WebAppFileLoaderTestFile.txt");
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

	@Test
	public void testWithServletContext(){
		ServletTester tester = new ServletTester();
		
		URL url = this.getClass().getResource("/");
		tester.setResourceBase(url.getPath());
		WebAppFileLoader fileloader = new WebAppFileLoader("context:/com/premiumminds/webapp/utils/WebAppFileLoaderTestFile.txt", tester.getContext().getServletContext());
		loadSuccess(fileloader);
	}
}
