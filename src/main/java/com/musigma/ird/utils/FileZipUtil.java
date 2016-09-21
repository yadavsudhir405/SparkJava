package com.musigma.ird.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author sudhir
 *         Date:19/9/16
 *         Time:5:33 PM
 *         Project:SparkJava
 */
public class FileZipUtil implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER= LoggerFactory.getLogger(FileZipUtil.class);
    private static final String CLASSFILES_DIRECTORY="/tmp/classFiles";
    private FileZipUtil() {
        throw new AssertionError("Object instantialtion is not allowed");
    }

    /**
     * creates a jar file on a location where class files are present
     *
     * @param source classFiles directory
     * @param jarName should ends with .jar
     */
    public static void createjarFile(String source,String jarName){
        FileOutputStream fos=null;
        ZipOutputStream zos=null;
        try {
             createDirectory(source);
             fos=new FileOutputStream(source+"/"+jarName);
             zos=new ZipOutputStream(fos);
             File classDirectory=new File(source);
             String[] classFiles=classDirectory.list();
             for(String classFile:classFiles){
                 if(classFile.contains(".class")){
                     addToZipFile(classFile, zos);
                 }

             }
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(),e);

        }catch(IOException e){
            LOGGER.error(e.getMessage(),e);
        }finally {
            if (zos!=null){
                try {
                    zos.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(),e);
                }
            }
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(),e);
                }
            }
        }
    }

    /**
     * This methods created the directory if missing.
     * @param path
     */
    private static void createDirectory(String path){
        File direFile=new File(path);
        if (!direFile.exists()){
            direFile.mkdirs();
        }
    }
    private static void addToZipFile(String fileName, ZipOutputStream zos) throws IOException {

        LOGGER.info("Writing '" + fileName + "' to zip file");

        File file = new File(CLASSFILES_DIRECTORY+"/"+fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }
}
