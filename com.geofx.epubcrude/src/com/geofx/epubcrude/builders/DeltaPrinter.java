/*
 * Copyright (c) 20013 Ric Wright - Geo-F/X
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
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

class DeltaPrinter implements IResourceDeltaVisitor
{
	public boolean visit(IResourceDelta delta)
	{
		IResource res = delta.getResource();
		switch (delta.getKind())
		{
			case IResourceDelta.ADDED:
				System.out.print("Resource ");
				System.out.print(res.getFullPath());
				System.out.println(" was added.");
				break;
			case IResourceDelta.REMOVED:
				System.out.print("Resource ");
				System.out.print(res.getFullPath());
				System.out.println(" was removed.");
				break;
			case IResourceDelta.CHANGED:
				System.out.print("Resource ");
				System.out.print(res.getFullPath());
				System.out.println(" has changed.");
				break;
		}
		return true; // visit the children
	}
 }
