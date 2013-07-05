/*
 * Copyright (c) 2009 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       BuilderReport.java
 *  Created:    27 November 2006
 * 
 *  A convenience class: gives static access to the project resources
 *  using the default Locale.
 *  
 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.validator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.adobe.epubcheck.api.Report;
import com.adobe.epubcheck.util.FeatureEnum;

public class BuilderReport implements Report
{
	IProject	project;
	
	public BuilderReport( IProject project, String ePubName )
	{
		//super(ePubName);
		this.project = project; 
	}

	public void error( String resourcePath, int line, String message  )
	{
		generateMarker(resourcePath, line, message,IMarker.PRIORITY_HIGH, IMarker.SEVERITY_ERROR);
		
		// System.err.println(ePubName + (resourcePath == null ? "" : "/" + resourcePath) + (line <= 0 ? "" : "(" + line + ")") + ": "
		//				+ message);	
	}


	public void warning( String resourcePath, int line, String message )
	{
		generateMarker(resourcePath, line, message, IMarker.PRIORITY_NORMAL, IMarker.SEVERITY_WARNING);
		
		// System.err.println(ePubName + (resourcePath == null ? "" : "/" + resourcePath) + (line <= 0 ? "" : "(" + line + ")")
		//				+ ": warning: " + message);
	}

	/**
	 * @param resourcePath
	 * @param line
	 * @param message
	 */
	private void generateMarker( String resourcePath, int line, String message, int priority, int severity )
	{
		if ( resourcePath != null && project != null )
		{
			
			IPath		path = Path.fromOSString(resourcePath);
			IResource 	resource = project.findMember(path);
			try
			{
				IMarker marker = resource.createMarker( IMarker.PROBLEM );
				marker.setAttribute( IMarker.LINE_NUMBER, line );
				marker.setAttribute( IMarker.MESSAGE, message );
				marker.setAttribute( IMarker.PRIORITY, priority );
				marker.setAttribute( IMarker.SEVERITY, severity );
			}
			catch (CoreException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void error(String resource, int line, int column, String message)
	{
		System.out.printf("Report.error: res: %s, line: %3d, col: %3d, msg: %s\n ", resource, line, column, message);
	}

	public void exception(String resource, Exception e)
	{
		System.out.printf("Report.exception: res: %s\n ", resource);		
	}

	public int getErrorCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int getExceptionCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int getWarningCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void hint(String resource, int line, int column, String message)
	{
		System.out.printf("Report.error: res: %s, line: %3d, col: %3d, msg: %s\n ", resource, line, column, message);		
		
	}

	public void info(String resource, FeatureEnum feature, String value)
	{
		System.out.printf("Report.error: res: %s, msg: %s\n ", resource, value);
	}

	public void warning(String resource, int line, int column, String message)
	{
		System.out.printf("Report.error: res: %s, line: %3d, col: %3d, msg: %s\n ", resource, line, column, message);		
	}

} 
