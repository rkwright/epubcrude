/*
 * Copyright (c) 2009 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       Validator.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.core.resources.IProject;

import com.adobe.epubcheck.api.EpubCheck;
import com.adobe.epubcheck.util.CheckUtil;

public class Validator
{
	public Validator()
	{		
		System.err.println("Wowser");
	}
	
	public boolean validate ( IProject project, String ePubName )
	{
		if (ePubName == "")
		{
			System.err.println("ePubName can't be empty!");
			return false;
		}

		BuilderReport report = null;

		try
		{		
			report = new BuilderReport(project, ePubName);
	
			// first check that the file is a zipfile and has the mimetype file
			FileInputStream epubIn = new FileInputStream(ePubName);

			byte[] header = new byte[58];

			if (epubIn.read(header) != header.length)
			{
				report.error(null, 0, "cannot read header");
			}
			else
			{
				if (header[0] != 'P' && header[1] != 'K')
				{
					report.error(null, 0, "corrupted ZIP header");
				}
				else if (!CheckUtil.checkString(header, 30, "mimetype"))
				{
					report.error(null, 0, "mimetype entry missing or not the first in archive");
				}
				else if (!CheckUtil.checkString(header, 38, "application/epub+zip"))
				{
					report.error(null, 0, "mimetype contains wrong type (application/epub+zip expected)");
				}
			}

			epubIn.close();

			// ok, it's a valid zipfile, so open it as such and try to check it
			//ZipFile zip = new ZipFile(new File(ePubName));

			//OCFChecker checker = new OCFChecker(zip, report);

			//checker.runChecks();

			//zip.close();
			
			File zipFile = new File(ePubName);
			EpubCheck checker = new EpubCheck(zipFile, report);
			checker.validate();
			
		}
		catch (IOException e)
		{
			report.error(null, 0, "I/O error: " + e.getMessage());
		}

		//report.flush();
		
		return true;
	}

}
