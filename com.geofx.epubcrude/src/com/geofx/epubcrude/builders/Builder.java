/*
 * Copyright (c) 2009 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 * 
 *  File:       Builder.java
 *  Created:    27 November 2006

 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.builders;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import com.geofx.epubcrude.plugin.PluginConstants;
import com.geofx.epubcrude.plugin.Resources;
import com.geofx.epubcrude.validator.Validator;



public class Builder extends IncrementalProjectBuilder
{
	private EPubFile		ePubFile;
	private Validator		validator;
	
	public Builder()
	{
		ePubFile = new EPubFile();
	   	validator = new Validator();
	}
	
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) 
	{
	}
	
	protected IProject[] build( int kind, Map args, IProgressMonitor monitor )
	{
		System.out.println("Build called");
		if (kind == IncrementalProjectBuilder.FULL_BUILD)
		{
			fullBuild(monitor);
		}
		else
		{
			IResourceDelta delta = getDelta(getProject());
			if (delta == null)
			{
				fullBuild(monitor);
			}
			else
			{
				fullBuild(monitor);
				//incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	/*
	private void incrementalBuild( IResourceDelta delta, IProgressMonitor monitor )
	{
		System.out.println("incremental build on " + delta);
		try
		{
			delta.accept(new IResourceDeltaVisitor()
			{
				public boolean visit( IResourceDelta delta )
				{
					System.out.println("changed: " + delta.getResource().getRawLocation());
					return true; // visit children too
				}
			});
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
	}
	*/

	private void fullBuild( IProgressMonitor monitor )
	{
		IProject project;
        String ePubName;
        
		try
		{
			project = getProject();
			ePubName = project.getPersistentProperty(PluginConstants.EPUBFILE_PROPERTY_NAME);
		
	        IPath	projectPath = project.getLocation();
	        // System.out.println("ePubName = " + ePubName + " path = " + projectPath.toOSString());

	        monitor.subTask(Resources.getString("eclipse.creatingfiles"));
	        
	        ePubFile.createEPub(projectPath.toOSString(), ePubName);
	          
	        // clear out the old ones before we create any new ones
	        project.deleteMarkers(null, true, IResource.DEPTH_INFINITE);
	        
	        // call the validator to find any new problems or warnings
            validator.validate(project, projectPath.toOSString() + "/" + ePubName);
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();			
		}	
	}
}
