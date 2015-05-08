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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import com.adobe.epubcheck.messages.Severity;
import com.adobe.epubcheck.util.FeatureEnum;

public class BuilderReport extends MasterReport
{
	protected IProject	project;
	protected int		errorCount = 0;
	protected int 		warningCount = 0;
	protected int 		exceptionCount = 0;
	protected int 		markerCount = 0;
	
	// create map for Severity to IMarker.  Unfortunately, IMarker only has 3 levels...
	private static final Map<Severity, Integer> SEVERITY_MAP = createMap();
	private static Map<Severity, Integer> createMap() {
		Map<Severity, Integer> aMap = new HashMap<Severity, Integer>();
        aMap.put(Severity.FATAL, IMarker.SEVERITY_ERROR);
        aMap.put(Severity.SUPPRESSED, IMarker.SEVERITY_ERROR);
        aMap.put(Severity.ERROR, IMarker.SEVERITY_ERROR);
        aMap.put(Severity.WARNING, IMarker.SEVERITY_WARNING);
        aMap.put(Severity.INFO, IMarker.SEVERITY_INFO);
        aMap.put(Severity.USAGE, IMarker.SEVERITY_INFO);
        return Collections.unmodifiableMap(aMap);
    }
	
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
			if (resource == null)
				resource = project;
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

	protected String getResourceLocation ( MessageLocation loc, Object... args ) 
	{
		String resourceLoc = loc.getFileName();
		String file = args.length > 0 ? args[0].toString() : "";

		if (file != null && file.length() > 0)
		{
			IPath	path = Path.fromOSString(file);
			if (project.findMember(path) != null)
				resourceLoc = file;
		}
		return resourceLoc;
	}
	
	public void message(MessageId arg0, MessageLocation loc, Object... args)
	{
		markerCount++;
		MessageDictionary dictionary = super.getDictionary();
		Message message = dictionary.getMessage(arg0);
		//String file = args.length > 0 ? args[0].toString() : "<null>";
		System.out.println("message(ID) called: " + message.getID() + " msg: '" + message.getMessage(args) + "' severity: " + message.getSeverity() + " suggestion: '" + message.getSuggestion() + "'");
		System.out.println("\tLocation: " + getResourceLocation(loc, args) + " line: " + loc.getLine() + " col: " + loc.getColumn() + " context: " + loc.getContext() + " count: " + markerCount);

		int	severity = SEVERITY_MAP.get(message.getSeverity());
		int	priority = message.getSeverity() == Severity.FATAL ? IMarker.PRIORITY_HIGH : IMarker.PRIORITY_NORMAL;
		
		generateMarker(getResourceLocation(loc, args), loc.getLine(), message.getMessage(args), priority, severity);

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
