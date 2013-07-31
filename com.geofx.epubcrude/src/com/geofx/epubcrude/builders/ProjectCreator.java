/*
 * Copyright (c) 2009 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       ProjectCreator.java
 *  Created:    27 November 2006

 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.builders;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

import com.geofx.epubcrude.plugin.Activator;
import com.geofx.epubcrude.plugin.PluginConstants;
import com.geofx.epubcrude.plugin.Resources;

public class ProjectCreator
{

	public void createProjectFromEPubFile(  String importFileName, IPath projectPath, String projectName, IProgressMonitor monitor )
	{
		monitor.beginTask(Resources.getString("eclipse.creatingproject"), 50);
		try
		{			
			// get the root, which is needed for all sorts of stuff
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			
			// create a monitor (progress dialog) for the task
			monitor.subTask(Resources.getString("eclipse.creatingdirectories"));
			
			// get project root
			IProject project = root.getProject( projectName );
			
			// get the "description" which is sort of like a manifest for the project
			IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(project.getName());
			
			// figure out where the project is on the local system and save it
			if (!Platform.getLocation().equals( projectPath ))
				description.setLocation( projectPath );
			
			// set the "nature" (type) of project to "ePub"
			description.setNatureIds(new String[] { PluginConstants.NATURE_ID });
			
			// so create the project
			project.create(description, monitor);
			
			// set what percent of the project is done, then open it
			monitor.worked(10);
			project.open(monitor);
			
			// add our builder to project so it gets called when the project needs building
			addBuilder( project, PluginConstants.BUILDER_ID );
			
			// increment progress some more
	  		monitor.worked(10);
			
	  		// get the plain filename (minus the path) from the source path and filename
			String ePubFileName = EPubFile.getFileName( importFileName );

			// save the filename in the project.  This will be serialized into the .project file for next time
			project.setPersistentProperty(PluginConstants.EPUBFILE_PROPERTY_NAME, ePubFileName );
		
			// get the singleton instance of the ePubFile object. It has quasi-static methods we want.
			EPubFile ePubFile = Activator.getDefault().getEPubFile();
			
			// here we extract the contents of the user's SOURCE ePub file and serialize them into the 
			// project's folder
		   	ePubFile.extractEPub( project.getLocation().toString(), importFileName );
				
			// causes the project tree in the Resource Navigator to be updated
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			
			dumpProjects();

		}
		catch (CoreException x)
		{
			x.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			monitor.done();
		}
	}
	
	public void createProjectFromExistingSource( String ePubFileName, IPath projectPath, String projectName, IProgressMonitor monitor )
	{
		monitor.beginTask(Resources.getString("eclipse.creatingproject"), 50);
		try
		{			
			// get the root, which is needed for all sorts of stuff
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			
			// create a monitor (progress dialog) for the task
			monitor.subTask(Resources.getString("eclipse.creatingdirectories"));
			
			// get project root
			IProject project = root.getProject( projectName );
			
			// get the "description" which is sort of like a manifest for the project
			IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(project.getName());
			
			// figure out where the project is on the local system and save it
			if (!Platform.getLocation().equals( projectPath ))
				description.setLocation( projectPath );
			
			// set the "nature" (type) of project to "ePub"
			description.setNatureIds(new String[] { PluginConstants.NATURE_ID });
			
			// so create the project
			project.create(description, monitor);
			
			// set what percent of the project is done, then open it
			monitor.worked(10);
			project.open(monitor);
			
			// add our builder to project so it gets called when the project needs building
			addBuilder( project, PluginConstants.BUILDER_ID );
			
			// increment progress some more
	  		monitor.worked(10);
			
	  		// get the plain filename (minus the path) from the source path and filename
			//String ePubFileName = EPubFile.getFileName( importFileName );

			// save the filename in the project.  This will be serialized into the .project file for next time
			project.setPersistentProperty(PluginConstants.EPUBFILE_PROPERTY_NAME, ePubFileName );
		
			// get the singleton instance of the ePubFile object. It has quasi-static methods we want.
			EPubFile ePubFile = Activator.getDefault().getEPubFile();
			
			// here we extract the contents of the user's SOURCE ePub file and serialize them into the 
			// project's folder
		   	// ePubFile.extractEPub( project.getLocation().toString(), importFileName );
								
			// causes the project tree in the Resource Navigator to be updated
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			
			dumpProjects();
			
		}
		catch (CoreException x)
		{
			x.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			monitor.done();
		}
	}
	
	/**
	 * Add the builder id to the project's description so it will get built
	 * 
	 * @param project
	 * @param id
	 */
	private void addBuilder( IProject project, String id )
	{
		IProjectDescription desc;
		try
		{
			desc = project.getDescription();

			ICommand[] commands = desc.getBuildSpec();
			for (int i = 0; i < commands.length; ++i)
				if (commands[i].getBuilderName().equals(id))
					return;

			System.err.printf("Adding %s to the %s project at %s\n", id, project.getName(), project.getFullPath());

			// add builder to project
			ICommand command = desc.newCommand();
			command.setBuilderName(id);
			ICommand[] nc = new ICommand[commands.length + 1];
			
			// Add it before other builders.
			System.arraycopy(commands, 0, nc, 1, commands.length);
			nc[0] = command;
			desc.setBuildSpec(nc);
			project.setDescription(desc, null);
		}

		catch (CoreException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 *  Dump out the list of projects in the workspace
	 * @throws CoreException 
	 */
	static public void dumpProjects() throws CoreException
	{
		IProject projects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		
		for ( int i=0; i<projects.length; i++ )
		{
			System.out.printf("i: %2d, name: %s,  path: %s\n", i, projects[i].getName(), projects[i].getFullPath());
			dumpBuilders(projects[i]);
		}

	}

	static private void dumpBuilders(IProject project) throws CoreException
	{
		ICommand[] commands = project.getDescription().getBuildSpec();
		for (int i = 0; i < commands.length; ++i)
		{
			System.out.printf("Project name: %s,  builder: %s\n", project.getName(), commands[i].getBuilderName());

		}
	}
			

}
