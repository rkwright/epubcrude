/*
 * Copyright (c) 2009-14 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       BuilderReport.java
 *  Created:    27 November 2009
 * 
 *  A convenience class: gives static access to the project resources
 *  using the default Locale.
 *  
 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.validator;

import java.io.File;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.adobe.epubcheck.api.MasterReport;
import com.adobe.epubcheck.messages.Message;
import com.adobe.epubcheck.messages.MessageDictionary;
import com.adobe.epubcheck.messages.MessageId;
import com.adobe.epubcheck.messages.MessageLocation;
import com.adobe.epubcheck.util.FeatureEnum;

public class BuilderReport extends MasterReport
{
	protected IProject	project;
	protected int		errorCount = 0;
	protected int 		warningCount = 0;
	protected int 		exceptionCount = 0;
	
	public BuilderReport( IProject project, String ePubName )
	{
		//super(ePubName);
		this.project = project; 
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
		
		generateMarker(resource, line, message,IMarker.PRIORITY_HIGH, IMarker.SEVERITY_ERROR);
		errorCount++;
	}

	public void exception(String resource, Exception e)
	{
		System.err.printf("Report.exception: res: %s\n ", resource);		
		exceptionCount++;
	}

	public int getErrorCount()
	{
		return errorCount;
	}

	public int getExceptionCount()
	{
		return exceptionCount;
	}

	public int getWarningCount()
	{
		return warningCount;
	}

	public void hint(String resource, int line, int column, String message)
	{
		System.out.printf("Report.hint: res: %s, line: %3d, col: %3d, msg: %s\n ", resource, line, column, message);		
		
	}

	public void info(String resource, FeatureEnum feature, String value)
	{
		//System.out.printf("Report.info: res: %s, msg: %s\n ", resource, value);
	}

	public void warning(String resource, int line, int column, String message)
	{
		//System.out.printf("Report.warning: res: %s, line: %3d, col: %3d, msg: %s\n ", resource, line, column, message);	
		
		generateMarker(resource, line, message, IMarker.PRIORITY_NORMAL, IMarker.SEVERITY_WARNING);
	}

	public void close()
	{
		System.out.println("Close called");
		// TODO Auto-generated method stub
		
	}

	public int generate()
	{
		System.out.println("generate called");
		// TODO Auto-generated method stub
		return 0;
	}

	public String getCustomMessageFile()
	{
		System.out.println("getCustomMessageFile called");
		// TODO Auto-generated method stub
		return null;
	}

	public MessageDictionary getDictionary()
	{
		System.out.println("getDictionary called");
		// TODO Auto-generated method stub
		return null;
	}

	public String getEpubFileName()
	{
		System.out.println("getEpubFileName called");
		// TODO Auto-generated method stub
		return null;
	}

	public int getFatalErrorCount()
	{
		System.out.println("getFatalErrorCount called");		
		// TODO Auto-generated method stub
		return 0;
	}

	public int getReportingLevel()
	{
		System.out.println("getReportingLevel called");

		// TODO Auto-generated method stub
		return 0;
	}

	public void initialize()
	{
		System.out.println("initialize called");

		// TODO Auto-generated method stub
		
	}

	public void message(MessageId arg0, MessageLocation loc, Object... args)
	{
		MessageDictionary dictionary = super.getDictionary();
		Message message = dictionary.getMessage(arg0);
		System.out.println("message(ID) called: " + message.getID() + " msg: '" + message.getMessage(args) + "' severity: " + message.getSeverity() + " suggestion: '" + message.getSuggestion() + "'");
		System.out.println("\tLocation: " + loc.getFileName() + " line: " + loc.getLine() + " col: " + loc.getColumn() + " context: " + loc.getContext());

		// TODO Auto-generated method stub
		
	}

	public void message(Message arg0, MessageLocation arg1, Object... args)
	{
		System.out.println("message called");
		// TODO Auto-generated method stub
		
	}

	public void setCustomMessageFile(String arg0)
	{
		System.out.println("setCustomMessage called");

		// TODO Auto-generated method stub
		
	}

	public void setEpubFileName(String arg0)
	{
		System.out.println("setEpubFileName called");

		// TODO Auto-generated method stub
		
	}

	public void setOverrideFile(File arg0)
	{
		System.out.println("setOverrideFile called");

		// TODO Auto-generated method stub
		
	}

	public void setReportingLevel(int arg0)
	{
		System.out.println("setReportingLevel called");

		// TODO Auto-generated method stub
		
	}

} 
