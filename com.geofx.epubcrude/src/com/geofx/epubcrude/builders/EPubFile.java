/*
 * Copyright (c) 2006-2014 Ric Wright - Geo-F/X
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 * 
 *  File:       EPUBFile.java
 *  Created:    27 November 2006

 * Contributors:
 *     Ric Wright - initial implementation
 *
 */

package com.geofx.epubcrude.builders;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * First pass at building a simple app to build ePub files.
 *
 */
public class EPubFile
{
    static final int        	BUFFER = 1024;
    static final String    	MIMETYPE_FILE = "mimetype";
    static final String		EPUB_MIMETYPE = "application/epub+zip";
    
    private String          	baseDir;
    private String          	zipFileName;
    private File 				zipFile = null;


    public EPubFile() {};

    /**
     * @param dir
     * @param name
     */
    public EPubFile( String dir, String zipName )
    {
        setBaseDir( dir );

        setZipFileName( zipName );
    }

    /** 
     * Uses the predefined baseDir and zipFileName to create a ePub archive
     * 
     * @return
     * @throws IOException
     */
    public boolean createEPub()  throws IOException
    {
        return createEPub( baseDir, zipFileName );
    }
     
    /**
     * Given a dir and filename, load the specified files and put 
     * them in zip-packaged ePub file.
     * 
     * @param dir
     * @param zipName
     * @return
     * @throws IOException
     */
    public boolean createEPub( String dir, String zipName ) throws IOException
    {
        FileOutputStream        fos = null;
        BufferedOutputStream    bos = null;
        ZipOutputStream         zos = null;

 
        // assign the class members 
        setBaseDir( dir );
        setZipFileName( zipName );
              
        try
        {
            zipFile = createZipFile(dir, zipName);

            //instantiate the ZipOutputStream
            fos = new FileOutputStream( zipFile );
            bos = new BufferedOutputStream(fos);
            zos = new ZipOutputStream(bos);

            File mimetypeFile = new File( baseDir + MIMETYPE_FILE );
            
            // if the mimetype file doesn't exist, then create it
            if (!mimetypeFile.exists() )
            {
            	createMimetypeFile(baseDir);
            }
            	
            //	Specifically add the "mimetype" file first
            writeZipFileEntry(zos, new File( baseDir + MIMETYPE_FILE ), ZipEntry.STORED );
 
            // Create all the zip-entries, recursing down the tree as necessary
            writeZipFileEntry(zos, new File(dir), ZipEntry.DEFLATED );
                        
            zos.finish();
        }
        catch (ZipException ze)
        {
            throw ze;
        }
        catch (FileNotFoundException fnfe)
        {
            throw fnfe;
        }

        catch (IOException ue)
        {
            throw ue;
        }
        finally
        {
            //close all the streams and the file
            if (bos != null)
                bos.close();
            if (fos != null)
                fos.close();
            if (zos != null)
                zos.close();
         //   if (zipFile != null)
         //       zipFile = null;
        }
        
        return true;
    }

    /**
     * Just a wrapper for the process of creating the actual zipfile
     * 
     * @param dir
     * @param zipName
     * @return the File object created
     * @throws IOException
     */
    private File createZipFile(String dir, String zipName) throws IOException
    {
        File zipFile;
        // Create the ZIP file itself
        zipFile = new File(dir + "/" + zipName);

        //check if it is a directory
        if (zipFile.isDirectory())
            throw new IOException("Invalid zip file [" + zipName + "]");

        //check if it is readonly            
        if (zipFile.exists() )
        {
            if (!zipFile.canWrite())
                throw new IOException("Existing Zip file is ReadOnly [" + zipName + "]");

            // overwrite the existing file
            zipFile.delete();
        }
        
        zipFile.createNewFile();
        return zipFile;
    }

    private void createMimetypeFile ( String baseDir ) throws IOException
    {
        FileOutputStream        fos = null;
        try
        {
            fos = new FileOutputStream( baseDir + MIMETYPE_FILE );
            
            fos.write( EPUB_MIMETYPE.getBytes() );
        }
        catch (IOException ioe)
        {
            throw ioe;
        }
        finally
        {
            if (fos != null)
                fos.close();
        }
    }
    
    /**
     * 
     * @param zos
     * @param fileName
     * @throws ZipException
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void writeZipFileEntry ( ZipOutputStream zos, File entryFile, int method )
                                                throws ZipException, FileNotFoundException, IOException
    {
        //check if the file is a directory.  If so, recurse into it and add it's entries            
        if (entryFile.isDirectory())
        {
            File[] entryFiles = entryFile.listFiles();
            for (int i = 0; i < entryFiles.length; i++)
            {
                String eName = getEntryName(entryFiles[i]);
 
                // don't add the archive to itself!  Also skip the "mimetype" file as we already added it first
                if ( !eName.equals( MIMETYPE_FILE ) && !eName.equals(zipFileName)  && !eName.startsWith(".") && !eName.contains(".DS_Store"))
                    writeZipFileEntry( zos, entryFiles[i], method );
            }
            return; 
        }   
        
        // get the entry name by stripping the base dir from the file's absolute path
        String  entryName = getEntryName( entryFile );
          
        BufferedInputStream bis = null;

        ZipEntry    zentry = null;
        byte[]      bArray = null;

        try
        {
              // check if the file doesn't exist            
            if (!entryFile.exists())
                throw new IOException("No such file [" + entryFile.getAbsolutePath() + "]");

            //check if the file is a directory            
            if (entryFile.isDirectory())
                throw new IOException("Invalid file, it's a directory! [" + entryFile.getAbsolutePath() + "]");

            // instantiate the BufferedInputStream
            bis = new BufferedInputStream(new FileInputStream(entryFile));

            // Get the content of the file and put into the byte[]
            int size = (int) entryFile.length();
            if (size == -1)
                throw new IOException("Cannot determine the file size [" + entryFile.getAbsolutePath() + "]");

            bArray = new byte[(int) size];
            int rb = 0;
            int chunk = 0;

            while (((int) size - rb) > 0)
            {
                chunk = bis.read(bArray, rb, (int) size - rb);

                if (chunk == -1)
                    break;

                rb += chunk;
            }

            //instantiate the CRC32
            CRC32 crc = new CRC32();
            crc.update(bArray, 0, size);

            //instantiate the ZipEntry
            zentry = new ZipEntry( entryName );
            zentry.setMethod( method );
            zentry.setSize(size);
            zentry.setCrc(crc.getValue());

            //write all the info to the ZipOutputStream
            zos.putNextEntry(zentry);
            zos.write(bArray, 0, size);
            zos.closeEntry();
        }
        catch (ZipException ze)
        {
            throw ze;
        }
        catch (FileNotFoundException fnfe)
        {
            throw fnfe;
        }
        catch (IOException ioe)
        {
            throw ioe;
        }

        finally
        {
            //close all the stream and file
            if (bis != null)
                bis.close();

            if (entryFile != null)
                entryFile = null;
        }//end of try-catch-finally
    }

    /**
     * Extract the zip-entry name from the absolute path name.
     * 
     * @param entryFile
     * @return
     */
    private String getEntryName ( File entryFile )
    {
        String  entryName = "";
        String  path = entryFile.getAbsolutePath().replace( File.separator, "/");
        int     basePos = path.indexOf( baseDir );
        if (basePos == 0)
        {
            entryName = path.substring( baseDir.length(), path.length());
            
            //  System.out.println("path: + " + path + " basePos:" + basePos + " entryName: " + entryName);
            
            entryName = entryName.replace( File.pathSeparator, "/");
        }
        
        return entryName;    
    }
    
    /**
     * Read and write out the contents of a ePub file.
     * 
     * @param dir - the destination to which the files are written
     * @param zipName - the name of the ePub file that is being extracted
     * @return
     */
    public boolean extractEPub( String dir, String zipName )
    {
        try
        {
            BufferedOutputStream    dest = null;
            BufferedInputStream     is = null;
            ZipEntry                entry;
            ZipFile                 zipfile = new ZipFile(zipName );
            Enumeration             e = zipfile.entries();
            
            while (e.hasMoreElements())
            {
                entry = (ZipEntry) e.nextElement();
                // System.out.println("Extracting: " + entry);

                String fileName = dir + "/" + entry.getName().replaceAll(File.pathSeparator, "/");

                if (entry.isDirectory())
                {
                	File file = new File(fileName);
                	if(!file.mkdir())
                	{
                		System.out.println("Unable to create dir: " + fileName);
                	}
                }
                else
                {
	                is = new BufferedInputStream(zipfile.getInputStream(entry));
	                int count;
	                byte data[] = new byte[BUFFER];
	
	                createDirs( fileName );
	                
	                FileOutputStream fos = new FileOutputStream( fileName );
	                dest = new BufferedOutputStream(fos, BUFFER);
	
	                while ((count = is.read(data, 0, BUFFER)) != -1)
	                {
	                    dest.write(data, 0, count);
	                }
	
	                dest.flush();
	                dest.close();
	                fos.close();
	                is.close();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Simple method to extract the path from a filename and ensure that 
     * the necessary chain of folders exist
     * 
     * @param fileName containg path that needs to be checked and created if needed
     * 
     * @return true if a folder needed to be created, else false
     */
    public boolean createDirs ( String fileName )
    {
    	boolean ret = false;   // assume the worst
    	try
    	{
	    	// extract the path from the path name
	        File file = new File(fileName);
	        String path = file.getAbsolutePath();
	        int	lastSep = path.lastIndexOf(File.separator);
	        if (lastSep > 0)
	        {
	        	path = path.substring(0, lastSep);
	        	File	filePath = new File(path);
	        	ret = filePath.mkdirs();
	        }
    	}
    	catch (Exception e)
    	{
    		
    	}
    	
    	return ret;
    }
    
    public boolean dumpePub()
    {
        return dumpePub( baseDir, zipFileName );
    }
    
    /**
     * Zoom through the entries in the zip file and dump out the info.
     * 
     */
    public boolean dumpePub( String dir, String zipName )
    {
        try
        {
            setBaseDir( dir );
            setZipFileName( zipName );
            
            BufferedInputStream is = null;
            ZipEntry entry;
            ZipFile zipfile = new ZipFile(dir + "/" + zipName );
            Enumeration e = zipfile.entries();

            while (e.hasMoreElements())
            {
                entry = (ZipEntry) e.nextElement();
                // System.out.println("Extracting: " + entry);

                is = new BufferedInputStream(zipfile.getInputStream(entry));

                // System.out.println("\tCompression type: " + entry.getMethod());
                // System.out.println("\tSize: " + entry.getSize());
                // System.out.println("\tCompressed size: " + entry.getCompressedSize());

                is.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * @param baseDir The baseDir to set.
     */
    public void setBaseDir(String dir)
    {
        if (!dir.endsWith(File.separator) && !dir.endsWith("/"))
            dir = dir + "/";
        
        this.baseDir = dir.replace( File.separator, "/");
    }
  
    /**
     * @param baseDir The baseDir to set.
     */
    static public String getFileName( String pathName )
    {
    	String	fileName = pathName;
        int 	n = pathName.lastIndexOf(File.separator);
        if ( n > 0)
        {
        	fileName = pathName.substring(n + 1); 
        }
        
        return fileName;
     }

  
    /**
     * @param zipFileName The zipFileName to set.
     */
    public void setZipFileName(String zipName)
    {
        this.zipFileName = zipName;
    }

	public File getZipFile()
	{
		return zipFile;
	}
}
