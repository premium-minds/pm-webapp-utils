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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import jakarta.servlet.ServletContext;

/**
 * Class to load resources of web applications
 * It can load from 
 *  - the application context (context:/path/to/file),
 *  - the application classpath (classpath:/com/package/ResourceName.txt),
 *  - the filesystem (filesystem:/path/to/file)
 * 
 * @author coolzero
 *
 */
public class WebAppFileLoader {
	private String filename;
	private ServletContext context;
	
	private static final String CONTEXT_SCHEME = "context";
	private static final String FILESYSTEM_SCHEME = "filesystem";
	private static final String CLASSPATH_SCHEME = "classpath";
	
	/**
	 * Create a file loader with a context 
	 * 
	 * @param filename - the URI to the file
	 * @param context - the servlet context needed for context:/
	 */
	public WebAppFileLoader(String filename, ServletContext context){
		this.filename = filename;
		this.context = context;
	}
	
	/**
	 * Create a file loader,
	 * 
	 * BUT THIS CAN'T USE THE CONTEXT!!!
	 * 
	 * @param filename - the URI to the file
	 */
	public WebAppFileLoader(String filename){
		this(filename, null);
	}
	
	/**
	 * Load the file
	 * 
	 * @return - InputStream with the file contents
	 * @throws URISyntaxException - if the URI is not valid
	 * @throws FileNotFoundException - if was not found
	 */
	public InputStream load() throws URISyntaxException, FileNotFoundException{
		URI uri = new URI(filename);
		
		if(uri.getScheme()==null) throw new URISyntaxException(filename, "filename must have a scheme");
		if(uri.getScheme().equals(FILESYSTEM_SCHEME)) return loadFilesystemScheme(uri);
		if(uri.getScheme().equals(CLASSPATH_SCHEME)) return loadClasspathScheme(uri);
		if(uri.getScheme().equals(CONTEXT_SCHEME)) return loadContextScheme(uri);

		throw new URISyntaxException(filename, "scheme not supported");
	}
	
	private InputStream loadFilesystemScheme(URI uri) throws FileNotFoundException{
		String filepath = uri.getSchemeSpecificPart();
		return new FileInputStream(filepath);
	}
	private InputStream loadClasspathScheme(URI uri) throws FileNotFoundException{
		String filepath = uri.getSchemeSpecificPart();
		InputStream stream = WebAppFileLoader.class.getResourceAsStream(filepath);
		if(stream==null) throw new FileNotFoundException();
		return stream;
	}
	private InputStream loadContextScheme(URI uri) throws FileNotFoundException{
		String filepath = uri.getSchemeSpecificPart();
		
		if(context==null) throw new RuntimeException("to load from the context, a context must be provided");
		
	    String realPath = context.getRealPath("/");
	    String fileSep = System.getProperty("file.separator");

	    if(realPath==null) throw new RuntimeException("couldn't get the real path from context");
	    //Make sure the real path ends with a file separator character ('/')
	    if (! realPath.endsWith(fileSep)){ realPath = realPath + fileSep; }
	    if (filepath.startsWith(fileSep)){ filepath = filepath.substring(1); }
	    
	    return new FileInputStream(realPath + filepath);		
	}
}
