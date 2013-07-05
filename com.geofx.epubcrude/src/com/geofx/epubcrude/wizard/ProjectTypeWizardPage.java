/*
 * Copyright (c) 2009 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 *
 *  File:       ProjectTypeWizardPage.java
 *  Created:    27 November 2006

 *  Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author riwright
 *
 */
public class ProjectTypeWizardPage extends WizardPage
{
	Button[] 	radios = new Button[4];
	int			projectType = 0;
	static int	IMPORT_EXISTING_EPUB = 0;
	static int	IMPORT_EXISTING_OEB  = 1;
	static int	IMPORT_EXISTING_HTML = 2;
	static int	CREATE_FROM_SCRATCH  = 3;
	


	protected ProjectTypeWizardPage(String pageName)
	{
		super(pageName);
		setTitle("Select Project Type");
		setDescription("This wizard helps the user select how the project will be created.");	
	}

	  
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl( Composite parent )
	{
		Composite controls = new Composite(parent, SWT.NULL);

		GridLayout layout = new GridLayout();
		
		controls.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 9;
		
		createRadioButton(controls, "Import existing &ePub document", 10, 5, 275, 40, IMPORT_EXISTING_EPUB, true);

		createRadioButton(controls, "Import existing &OEBPS document", 10, 30, 275, 40, IMPORT_EXISTING_OEB, false);

		createRadioButton(controls, "Import existing &HTML document", 10, 55, 275, 40, IMPORT_EXISTING_HTML, false);
	    
	    createRadioButton(controls, "&Create ePub document from scratch", 10, 75, 275, 40, CREATE_FROM_SCRATCH, false);	   
	    
		setControl(controls);
	}

	void createRadioButton( Composite parent, String label, int x, int y, int width, int height, final int index, boolean selected )
	{
	    radios[index] = new Button(parent, SWT.RADIO);
	    radios[index].setText( label );
	    radios[index].setBounds(x, y, width, height );
	    radios[index].setSelection( selected );
	    radios[index].addSelectionListener( new SelectionListener()
		{
			public void widgetDefaultSelected( SelectionEvent e )
			{				
			}

			public void widgetSelected( SelectionEvent e )
			{
				setRadioSelection( index );
			}
		});	
	
	}
	
	private void setRadioSelection( int i )
	{
		projectType = i;
		System.out.println("index is " + i );
	}
	
	protected void dialogChanged()
	{
		// TODO Auto-generated method stub
		
	}


	public int getProjectType()
	{
		return projectType;
	}

}
