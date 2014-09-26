/*
 * Copyright (c) 2009-14 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       ImportEPubWizard.java
 *  Created:    27 November 2009
 * 
 *  A convenience class: gives static access to the project resources
 *  using the default Locale.
 *  
 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.plugin;

import java.util.*;

public final class Resources
{
   /**
    * the ResourceBundle for this project
    */
   public static final ResourceBundle resources =
      ResourceBundle.getBundle("com.geofx.epubcrude.plugin.Strings");

   /**
    * returns the resource string associated with the key
    * 
    * @param key the resource key
    * @return the resource string
    * @throw MissingResourceException if the resource is missing
    */
   public static final String getString(String key)
      throws MissingResourceException
   {
      return resources.getString(key);
   }
}
