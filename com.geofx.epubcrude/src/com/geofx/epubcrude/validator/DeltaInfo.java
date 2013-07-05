/*
 * Copyright (c) 2009 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       DeltaInfo.java
 *  Created:    27 November 2006
 * 
 *  A convenience class: gives static access to the project resources
 *  using the default Locale.
 *  
 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.validator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

public class DeltaInfo implements IResourceDeltaVisitor
{
	private IProject	project;
	private String		resourceName;
	private IResource	resource;
	
	public boolean visit(IResourceDelta delta) 
    {
       IResource res = delta.getResource();
       if (delta.getKind() ==  IResourceDelta.CHANGED )             
             storeResource( res );
       
       return true; // visit the children
    }

	private void storeResource ( IResource res )
	{
		project = res.getProject();
		
		if (project != null)
		{
			resourceName = res.getName();
			resource = res;
		
			System.out.println(" Project: " +  project.getName() + " ResourceName: " + resourceName);
		} 
	}

	public IProject getProject()
	{
		return project;
	}

	public IResource getResource()
	{
		return resource;
	}

	public String getResourceName()
	{
		return resourceName;
	}
}
