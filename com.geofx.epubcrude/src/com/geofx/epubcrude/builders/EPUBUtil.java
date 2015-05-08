/*
 * Copyright (c) 2009-14 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       EPUBUtil.java
 *  Created:    27 November 2009

 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.builders;

import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.geofx.epubcrude.plugin.PluginConstants;

public class EPUBUtil implements IHandler
{

	public void addHandlerListener(IHandlerListener handlerListener)
	{
		System.out.printf("Got commands\n");

	}

	public void dispose()
	{
		System.out.printf("Dispose\n");

	}

	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		IProject	project = getSelectedProject();
		if (project == null)
		{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", "Please select a project");
			return null;
		}

		//IFile	epubFile = project.getFile(getEPUBName(project));

		
		//get object which represents the workspace  
		//IWorkspace workspace = ResourcesPlugin.getWorkspace();  

		//get location of workspace (java.io.File)  
		//File workspaceDirectory = workspace.getRoot().getLocation().toFile();
		
		InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(), "", 
								"Enter new name for the EPUB file for " + project.getName(), getEPUBName(project), new NameChecker());

		if (dlg.open() == Window.OK)
		{			
			String newName = dlg.getValue();
			if (newName.indexOf(".epub") > 0 || newName.indexOf(".EPUB") > 0)
			{
				// save the filename in the project object. This will be serialized into the .project file 
				saveEPUBName(project, newName);				
			}
			else
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", "Invalid output name, must be .epub or .EPUB");
		}
		
		return null;
	}

	public boolean isEnabled()
	{
		return true;
	}

	public boolean isHandled()
	{
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener)
	{
	}

	static public void saveEPUBName ( IProject project, String ePubName )
	{
		try
		{
			IProjectDescription description = project.getDescription();
		
			ICommand command = description.newCommand();
			command.setBuilderName(PluginConstants.BUILDER_ID);

			Map<String,String>  nameMap = command.getArguments();
			nameMap.put(PluginConstants.EPUBFILE_NAME, ePubName);
			command.setArguments(nameMap);
					
			ICommand[] commands = new ICommand[1];
			commands[0] = command;
			
			description.setBuildSpec(commands);
			project.setDescription(description, null);
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}

	}
	
	static public String getEPUBName ( IProject project )
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

		//System.out.println(".project ePubName: " + ePubName);
		return ePubName;
	}
	
	protected IProject getSelectedProject()
	{
		
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			
		IStructuredSelection selection = (IStructuredSelection)window.getSelectionService().getSelection(IPageLayout.ID_PROJECT_EXPLORER);
		
		IProject project = null;
			
		if (selection != null && selection.getFirstElement() instanceof IProject )
		{
			project = (IProject) selection.getFirstElement();
		}
					    
		return project;
	}
	/**
	 * This class validates a String. It makes sure that the String is between 5 and 8
	 * characters
	 */
	class NameChecker implements IInputValidator
	{
		/**
		 * Validates the String. Returns null for no error, or an error message
		 * 
		 * @param newText
		 *            the String to validate
		 * @return String
		 */
		public String isValid(String newText)
		{
			int len = newText.length();

			// Determine if input is too short or too long
			if (len < 1)
				return "Too short";
			if (len > 128)
				return "Too long";

			// Input must be OK
			return null;
		}
	}
}
