/*
 * Copyright (c) 2009-14 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 * 
 *  File:       GetNewEpubName.java
 *  Created:    27 November 2009

 * Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class GetNewEPubNameWizardPage extends WizardPage
{

	String 	fileName = "";

	protected GetNewEPubNameWizardPage(String pageName)
	{
		super(pageName);

		setTitle("Select ePub Document");
		setDescription("Select which ePub document should be imported.");	
	}

	public void createControl( Composite parent )
	{
		final Composite controls = new Composite(parent, SWT.NULL);

		GridLayout layout = new GridLayout();
		
		controls.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 9;
				
		Label label = new Label(controls, SWT.NULL);
	    label.setText("Name of new ePub file:");		
	    
		final Text fileText = new Text(controls, SWT.SINGLE | SWT.BORDER);
		fileText.setBounds(0, 0, 500, 20);
		fileText.setText("");

		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.grabExcessHorizontalSpace = true;
	    
		
		fileText.setLayoutData(gridData);	
		
		fileText.addModifyListener(new ModifyListener(){

			public void modifyText( ModifyEvent e )
			{
				fileName = fileText.getText();
				System.out.println("FileName = " + fileName);
			}
			
		});
		
		
		
		setControl(controls);
	}

	public String getFileName()
	{
		return fileName;
	}

}
