/*
 * Copyright (c) 2006-2013 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 * 
 *  File:       NewProjectWizard.java
 *  Created:    27 November 2006
 *  
 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.wizard;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import com.geofx.epubcrude.plugin.Activator;
import com.geofx.epubcrude.plugin.PluginConstants;
import com.geofx.epubcrude.plugin.PluginTools;
import com.geofx.epubcrude.plugin.Resources;

public class NewProjectWizard extends Wizard implements INewWizard
{
	/**
	 * The main page on the wizard: collects the project name and location.
	 */
	private WizardNewProjectCreationPage	newProjectPage;
	private ProjectTypeWizardPage			projectTypePage;
	private	 ImportEPubWizardPage			ePubImportPage;
	private	 GetNewEPubNameWizardPage		newEPubNamePage;
	
	public NewProjectWizard()
	{

		// TODO Auto-generated constructor stub
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init( IWorkbench workbench, IStructuredSelection selection )
	{
		setNeedsProgressMonitor(true);
	}

	/**
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages()
	{
		try
		{
			super.addPages();
					
			newProjectPage = new WizardNewProjectCreationPage("BasicNewProject");
			newProjectPage.setTitle(Resources.getString("eclipse.newprojectname"));
			newProjectPage.setDescription(Resources.getString("eclipse.newprojectdescription"));
            			  
	        URL installURL = Activator.getDefault().getBundle().getEntry("/");
	        System.out.println("installURL: " + installURL );
	        URL url = new URL(installURL, "icons/EPUB-Logo-48.gif");
            newProjectPage.setImageDescriptor(ImageDescriptor.createFromURL(url)); 

			addPage(newProjectPage);  
			
			projectTypePage = new ProjectTypeWizardPage(PluginConstants.PROJECT_TYPE_PAGE);
			addPage( projectTypePage );  
						
			ePubImportPage = new ImportEPubWizardPage(PluginConstants.EPUB_IMPORT_PAGE);
			addPage(ePubImportPage);
			
		}
		catch (Exception x)
		{
			reportError(x);
		}
	}
	
	@Override
	public IWizardPage getNextPage( IWizardPage page )
	{
		System.out.println("Request to get next page: " + page.getName());
		return super.getNextPage(page);
	}
	
	@Override
	public boolean performFinish()
	{
		try
		{
			WorkspaceModifyOperation op = new WorkspaceModifyOperation()
			{
				@Override
				protected void execute( IProgressMonitor monitor ) throws CoreException, InvocationTargetException,
								InterruptedException
				{
					createProject( newProjectPage.getLocationPath(), newProjectPage.getProjectName(),
									monitor != null ? monitor : new NullProgressMonitor());
				}
			};
			getContainer().run(false, true, op);
		}
		catch (InvocationTargetException x)
		{
			reportError(x);
			return false;
		}
		catch (InterruptedException x)
		{
			reportError(x);
			return false;
		}
		return true;
	}

	/**
	 * This is the actual implementation for project creation.
	 * @param monitor reports progress on this object
	 */
	protected void createProject(  IPath projectPath, String projectName, IProgressMonitor monitor )
	{
		if (projectTypePage.getProjectType() == ProjectTypeWizardPage.IMPORT_EXISTING_EPUB)
			Activator.getDefault().getBuilder().createProjectFromEPubFile( ePubImportPage.getFileName(), projectPath, projectName, monitor );
		else 
			if (projectTypePage.getProjectType() == ProjectTypeWizardPage.IMPORT_EXISTING_OEB)
			Activator.getDefault().getBuilder().createProjectFromExistingSource( newEPubNamePage.getFileName(), projectPath, projectName, monitor );
			
	}


	/**
	 * Displays an error that occured during the project creation.
	 * @param x details on the error
	 */
	private void reportError( Exception x )
	{
		ErrorDialog.openError(getShell(), Resources.getString("eclipse.dialogtitle"), Resources.getString("eclipse.projecterror"),
						PluginTools.makeStatus(x));
	}

	/**
	 * Helper method: it recursively creates a folder path.
	 * @param folder
	 * @param monitor
	 * @throws CoreException
	 * @see java.io.File#mkdirs()
	 */
	/*
	private void createFolderHelper( IFolder folder, IProgressMonitor monitor ) throws CoreException
	{
		if (!folder.exists())
		{
			IContainer parent = folder.getParent();
			if (parent instanceof IFolder && (!((IFolder) parent).exists()))
				createFolderHelper((IFolder) parent, monitor);
			folder.create(false, true, monitor);
		}
	}
	*/
}
