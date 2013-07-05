/*
 * Copyright (c) 2009 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 * 
 *  File:       Activator.java
 *  Created:    27 November 2006
 *
 * Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.plugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.geofx.epubcrude.builders.EPubFile;
import com.geofx.epubcrude.builders.ProjectBuilder;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{

	// The plug-in ID
	public static final String	PLUGIN_ID	= "com.geofx.epubcrude.plugin";
	private ProjectBuilder			builder;
	private EPubFile				ePubFile;

	// The shared instance
	private static Activator	plugin;

	/**
	 * The constructor
	 */
	public Activator()
	{
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start( BundleContext context ) throws Exception
	{
		super.start(context);
		
		builder = new ProjectBuilder();
		ePubFile = new EPubFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop( BundleContext context ) throws Exception
	{
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault()
	{
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor( String path )
	{
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public ProjectBuilder getBuilder()
	{
		return builder;
	}

	public EPubFile getEPubFile()
	{
		return ePubFile;
	}
}
