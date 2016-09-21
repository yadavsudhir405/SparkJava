package com.musigma.ird.sparkjava.core;

import com.musigma.ird.utils.ClassGenerator;
import com.musigma.ird.utils.FileZipUtil;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author sudhir
 *         Date:21/9/16
 *         Time:12:41 AM
 *         Project:SparkJava
 */
public enum JavaAssistClassManager {
    INSTANCE;
    private static final Logger LOGGER= LoggerFactory.getLogger(JavaAssistClassManager.class);
    private final static Map<String,Object> POOL =new ConcurrentHashMap<String,Object>();


    public  Object getObjectFromClassName(final String className,List<Field> fieldList){
        Object obj= POOL.get(className);
        if(obj==null){
            obj=generateClassFile(className,fieldList);
            POOL.put(className,obj);
            makejar(className);
        }
        return obj;
    }

    private Object generateClassFile(String className,List<Field> fieldList) {
        return ClassGenerator.getObjectWithFields(className,fieldList);
    }


    public boolean isClassAlredylaodedWithClassName(final String className){
        return POOL.containsKey(className)?true:false;
    }


    private  String  makejar(String jarName){
        String fileName=jarName+".jar";
        LOGGER.info("Creating jar file with jar name as "+fileName);
        FileZipUtil.createjarFile("/tmp/classFiles",fileName);
        return fileName;

    }
}
