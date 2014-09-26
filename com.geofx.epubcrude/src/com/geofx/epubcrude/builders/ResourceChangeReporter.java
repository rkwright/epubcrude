/*
 * Copyright (c) 2013-14 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 * 
 *  File:       Builder.java
 *  Created:    20 June 2013

 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.builders;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;

public class ResourceChangeReporter implements IResourceChangeListener
{
	public void resourceChanged(IResourceChangeEvent event)
	{
		IResource res = event.getResource();
		try
		{

			switch (event.getType())
			{
				case IResourceChangeEvent.PRE_CLOSE:
					System.out.print("RCR: Project ");
					System.out.print(res.getFullPath());
					System.out.println(" is about to close.");
					break;
				case IResourceChangeEvent.PRE_DELETE:
					System.out.print("RCR: Project ");
					System.out.print(res.getFullPath());
					System.out.println(" is about to be deleted.");
					break;
				case IResourceChangeEvent.POST_CHANGE:
					System.out.println("RCR: Resources have changed.");
					event.getDelta().accept(new DeltaPrinter());

					break;
				case IResourceChangeEvent.PRE_BUILD:
					System.out.println("RCR: Build about to run.");
					event.getDelta().accept(new DeltaPrinter());
					break;
				case IResourceChangeEvent.POST_BUILD:
					System.out.println("RCR: Build complete.");
					event.getDelta().accept(new DeltaPrinter());
					break;
			}
		}
		catch (CoreException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}