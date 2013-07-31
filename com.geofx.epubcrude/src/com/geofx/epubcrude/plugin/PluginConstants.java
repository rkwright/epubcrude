/*
 * Copyright (c) 2009 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       PluginConstants.java
 *  Created:    27 November 2006

 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */
package com.geofx.epubcrude.plugin;

import org.eclipse.core.runtime.QualifiedName;

/**
 * A placeholder for the various constants needed throughout the plugin
 * project. Most of these are references to ids defined in the plugin.xml
 * file.
 */
public final class PluginConstants
{
	/**
	 * plugin id from plugin.xml
	 */
	public static final String			PLUGIN_ID				= "com.geofx.epubcrude.plugin";

	/**
	 * console (MessengerView) id from plugin.xml
	 */
	public static final String			CONSOLE_ID				= "com.geofx.epubcrude.plugin.views.console";

	/**
	 * ProjectCreator id from plugin.xml
	 */
	public static final String			BUILDER_ID				= "com.geofx.epubcrude.builder";

	/**
	 * project nature (for .project)
	 */
	public static final String			NATURE_ID				= "com.geofx.epubcrude.plugin.nature";
	
	/**
	 * property local name for the build status
	 */
	public static final String			BUILD_PROPERTY			= "build";
	
	/**
	 * namespace URI for the properties
	 */
	public static final String			PROPERTY_NAMESPACE		= "http://www.readium.org/epubcrude";
	
	/**
	 * property name for the ePubFile name for the project, i.e. the output file
	 */
	public static final String			EPUBFILE_NAME			= "ePubFileName";
	

	/**
	 * name for the wizard page for selecting the project type
	 */
	public static final String			PROJECT_TYPE_PAGE		= "ProjectTypePage";

	/**
	 * name for the wizard page for specifying the ePub file to import
	 */
	public static final String			EPUB_IMPORT_PAGE		= "ePubImportPage";
	
	/**
	 * name for the wizard page for selecting the project type
	 */
	public static final String			EPUB_FILENAME_PAGE		= "ePubFilenamePage";	

	/**
	 * property qualified name for the epub filename
	 */	
	public static final QualifiedName	EPUBFILE_PROPERTY_NAME	= new QualifiedName(PROPERTY_NAMESPACE, EPUBFILE_NAME);
	
	/**
	 * property qualified name for the build status
	 */
	public static final QualifiedName	BUILD_PROPERTY_NAME		= new QualifiedName(PROPERTY_NAMESPACE, BUILD_PROPERTY);
}
