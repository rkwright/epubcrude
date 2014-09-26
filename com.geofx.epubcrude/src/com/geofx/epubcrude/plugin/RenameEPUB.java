package com.geofx.epubcrude.plugin;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

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
		//final Label label = new Label(composite, SWT.NONE);
		//label.setText("This will display the user input from InputDialog");

		InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(), "", "Enter new name for the EPUB file", "Current name",
				new NameChecker());
		if (dlg.open() == Window.OK)
		{
			// User clicked OK; update the label with the input
			
			
			// get the root, which is needed for all sorts of stuff
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			
			String		command = dlg.getValue();
			String[] 	parts;
			
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
			// save the filename in the project. This will be serialized into the .project file for
			// next time
			try
			{
				project.getPersistentProperty(PluginConstants.EPUBFILE_PROPERTY_NAME );

				project.setPersistentProperty(PluginConstants.EPUBFILE_PROPERTY_NAME, parts[1]);
			}
			catch (CoreException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
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
