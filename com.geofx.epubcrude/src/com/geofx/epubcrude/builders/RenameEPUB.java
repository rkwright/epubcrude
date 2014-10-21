/*
 * Copyright (c) 2009-14 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       PluginTools.java
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
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import com.geofx.epubcrude.plugin.PluginConstants;

public class RenameEPUB implements IHandler
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
		// InputDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(),
		// "Info", "Info for you");
		// Create a label to display what the user typed in
		// final Label label = new Label(composite, SWT.NONE);
		// label.setText("This will display the user input from InputDialog");

		InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(), "", 
								"Enter new name for the EPUB file",	"Current name", new NameChecker());

		if (dlg.open() == Window.OK)
		{
			// User clicked OK; update the label with the input

			// get the root, which is needed for all sorts of stuff
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

			String command = dlg.getValue();
			String[] parts;

			if (command.contains(","))
			{
				parts = command.split(",");
			}
			else
			{
				throw new IllegalArgumentException("String " + command + " does not contain two strings-");
			}

			// get project root
			IProject project = root.getProject(parts[0]);

			// save the filename in the project object. This will be serialized into the .project 
			// file for next time

			saveEPUBName(project, parts[1]);

			/*
			 * try { project.getPersistentProperty(PluginConstants.EPUBFILE_PROPERTY_NAME );
			 * 
			 * project.setPersistentProperty(PluginConstants.EPUBFILE_PROPERTY_NAME, parts[1]); }
			 * catch (CoreException e) { e.printStackTrace(); }
			 * 
			 * return null;
			 */

		}
		return null;
	}

	public boolean isEnabled()
	{
		//System.out.printf("Is enabled?\n");
		return true;
	}

	public boolean isHandled()
	{
		//System.out.printf("Is handled\n");
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

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
			
			//ICommand[] commands = description.getBuildSpec();
			
			ICommand[] commands = new ICommand[1];
			//System.arraycopy(commands, 0, newCommand, 1, commands.length);
			commands[0] = command;
			
			description.setBuildSpec(commands);
			project.setDescription(description, null);
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}

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
