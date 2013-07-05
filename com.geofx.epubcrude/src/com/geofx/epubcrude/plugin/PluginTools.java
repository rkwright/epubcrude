/*
 * Copyright (c) 2009 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       PluginTools.java
 *  Created:    27 November 2006

 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.plugin;

import org.eclipse.core.runtime.*;


/**
 * Utility methods for the project.
 */
public class PluginTools
{
   /**
    * Create an IStatus object from an exception.    * @param x exception to process    * @return IStatus status object for the above exception    */

   public static IStatus makeStatus(Exception x)
   {
      if (x instanceof CoreException)
         return ((CoreException)x).getStatus();
      else
         return new Status(IStatus.ERROR,
                            PluginConstants.PLUGIN_ID,
                            IStatus.ERROR,
                            x.getMessage(),
                            x);
   }
  
}