package com.musigma.ird.utils;

import com.musigma.ird.sparkjava.core.Field;
import javassist.*;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author sudhir
 *         Date:19/9/16
 *         Time:5:10 PM
 *         Project:SparkJava
 */
public class ClassGenerator implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CLASS_FILE_DIRECTORY="/tmp/classFiles";
    private static final Logger LOGGER= LoggerFactory.getLogger(ClassGenerator.class);
    private static String PACKAGE_DIRECTORY="com.musigma.ird.sparkjava.core";
    public static final ClassPool CLASS_POOL=ClassPool.getDefault();
    private static  final  Map<String,Object> CLASS_OBJECT_MAPPER=new ConcurrentHashMap<String,Object>();
    public static Object getObjectWithFields(String className, List<Field> fields ){

        Object object=null;
        CtClass modelClass= getCtClass(className);
        if (modelClass!=null){
            LOGGER.info(className+" is already into to the ctclassPool,Returning old Obejct");
            return CLASS_OBJECT_MAPPER.get(className);
        }else {
            LOGGER.info(className+" is missing from the ctclassPool,Returing the new Obejct Instanece from new classfile");
            CtClass intefaceCtClass = null;
            try {
                intefaceCtClass = CLASS_POOL.get("java.io.Serializable");
                modelClass = CLASS_POOL.makeClass(className);
                modelClass.addInterface(intefaceCtClass);
                for (Field field : fields) {
                    String str = field.getName();
                    modelClass.addField(CtField.make("public " + field.getType().toString() + "  " + str + ";", modelClass));
                    String methodName = (str.charAt(0) + "").toUpperCase() + str.substring(1);
                    modelClass.addMethod(CtMethod.make("public " + field.getType().toString() + " get" + methodName + "(){ return " + str + ";}", modelClass));
                    modelClass.addMethod(CtMethod.make("public void set" + methodName + "(" + field.getType().toString() + " " + str + "){this." + str + "=" + str + ";}", modelClass));
                }
                object=getInstanceFromCtClass(modelClass);
                CLASS_OBJECT_MAPPER.put(className,object);
                modelClass.writeFile(CLASS_FILE_DIRECTORY);
                modelClass.toBytecode();
            } catch (NotFoundException e1) {
                LOGGER.error(e1.getMessage(), e1);
            } catch (CannotCompileException e) {
                LOGGER.error(e.getMessage(), e);
            }catch(IOException e){
                LOGGER.error(e.getMessage(),e);
            }
            return object;
        }
    }
    private static CtClass getCtClass(String className){

        try {
            return  CLASS_POOL.getCtClass(className);
        } catch (NotFoundException e) {
            //No Need to show into the error log,
        }
       return null;
    }
    private static Object getInstanceFromCtClass(CtClass ctClass){
        try {
            Class<?> newClass=ctClass.toClass();
            return getInstanceFromClass(newClass);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return  null;
    }
    private static  Object getInstanceFromClass(Class<?> classs){
        try {
            return  classs.newInstance();
        } catch (InstantiationException e) {
          LOGGER.error(e.getMessage(),e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }
}
