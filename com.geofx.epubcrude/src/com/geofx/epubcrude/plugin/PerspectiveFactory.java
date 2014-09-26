/*
 * Copyright (c) 2009-14 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       PerspectiveFactory.java
 *  Created:    27 November 2009
 *
 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */
package com.geofx.epubcrude.plugin;

import org.eclipse.ui.*;

public class PerspectiveFactory implements IPerspectiveFactory
{	
	public void createInitialLayout( IPageLayout layout )
	{
	
		
		// Get the editor area.
		 String editorArea = layout.getEditorArea();

		 // Top left: Project Explorer view and Bookmarks view placeholder
		 IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.25f, editorArea);
		 topLeft.addView(IPageLayout.ID_PROJECT_EXPLORER);

		 // Bottom: Problems and console view 
	  	 IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.80f, editorArea);
		 bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		 bottom.addView( "org.eclipse.ui.console.ConsoleView");
		 
		// Right: Outline view 
		 IFolderLayout topRight = layout.createFolder("topRight", IPageLayout.RIGHT, 0.75f, editorArea);
		 topRight.addView(IPageLayout.ID_OUTLINE);
	}
}
