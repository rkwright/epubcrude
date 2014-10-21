/*
 * Copyright (c) 2009-14 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 * 
 *  File:       Builder.java
 *  Created:    27 November 2009

 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.builders;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
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
		if (ePubFile == null)
		{
			ePubFile = new EPubFile();
		   	validator = new Validator();
		 
			System.out.println("Build ctor - creating ResourceChangeListener");
	
			/* we don't need to actually listen for changes.  This is for debugging only
		    IResourceChangeListener listener = new ResourceChangeReporter();
		    ResourcesPlugin.getWorkspace().addResourceChangeListener(listener,
		       IResourceChangeEvent.PRE_CLOSE
		       | IResourceChangeEvent.PRE_DELETE
		       | IResourceChangeEvent.PRE_BUILD
		       | IResourceChangeEvent.POST_BUILD
		       | IResourceChangeEvent.POST_CHANGE);
		    */
		}

	}
	
	
	protected IProject[] build( int kind, Map args, IProgressMonitor monitor )
	{
		System.err.println("Build called");
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
				// we don't have any way to tell what impact a changed resource will have on the whole EPUB
				// so we always do a full "build"	
				fullBuild(monitor);
				//incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	
	/* we don't need incremental builds
	private void incrementalBuild( IResourceDelta delta, IProgressMonitor monitor )
	{
		System.err.println("incremental build on " + delta);
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
			
			fullBuild(monitor);

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

		System.err.println("Full build invoked");

		try
		{
			project = getProject();
			
			ePubName = getEPUBName(project);
			/*
			  if (ePubName == null || ePubName == "")
			{
				ePubName = project.getPersistentProperty(PluginConstants.EPUBFILE_PROPERTY_NAME);
				System.out.println("persistent Property ePubName: " + ePubName);
			}
			
			RenameEPUB.saveEPUBName(project, ePubName);
			*/
			
	        IPath	projectPath = project.getLocation();

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
	
	public void saveEPUBName ( IProject project, String ePubName )
	{
		try
		{
			IProjectDescription description = project.getDescription();
		
			ICommand command = description.newCommand();

			Map<String,String>  nameMap = command.getArguments();
			nameMap.put(PluginConstants.EPUBFILE_NAME, ePubName);
			command.setArguments(nameMap);
			
			ICommand[] commands = description.getBuildSpec();
			
			ICommand[] newCommand = new ICommand[commands.length + 1];
			System.arraycopy(commands, 0, newCommand, 1, commands.length);
			newCommand[0] = command;
			
			description.setBuildSpec(newCommand);
			project.setDescription(description, null);
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}

	}
	
	public String getEPUBName ( IProject project )
	{
		String	ePubName = "";

		try
		{
			IProjectDescription description = project.getDescription();			
	
			ICommand[] 	commands = description.getBuildSpec();
			
			if (commands.length> 0)
			{
				ICommand command = commands[0];
				Map<String,String>  nameMap = command.getArguments();
				ePubName = nameMap.get(PluginConstants.EPUBFILE_NAME);
			}
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}

		System.out.println(".project ePubName: " + ePubName);
		return ePubName;
	}
}
