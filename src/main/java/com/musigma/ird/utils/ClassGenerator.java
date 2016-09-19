package com.musigma.ird.utils;

import com.musigma.ird.sparkjava.core.Field;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author sudhir
 *         Date:19/9/16
 *         Time:5:10 PM
 *         Project:SparkJava
 */
public class ClassGenerator {
    private static final String CLASS_FILE_DIRECTORY="/tmp/classFiles";
    private static final Logger LOGGER= LoggerFactory.getLogger(ClassGenerator.class);
    public static Object getObjectWithFields(String className, List<Field> fields ){

        ClassPool classPool=ClassPool.getDefault();

        CtClass intefaceCtClass=null;
        try {
            intefaceCtClass = classPool.get("java.io.Serializable");
        } catch (NotFoundException e1) {
            // TODO Auto-generated catch block
            LOGGER.error(e1.getMessage(),e1);
        }
        CtClass employeeClass=classPool.makeClass(className);
        employeeClass.addInterface(intefaceCtClass);
        try {
            for(Field field:fields){
                String str=field.getName();
                employeeClass.addField(CtField.make(field.getType().toString()+"  "+str+";", employeeClass));
                String methodName=(str.charAt(0)+"").toUpperCase()+str.substring(1);
                employeeClass.addMethod(CtMethod.make(field.getType().toString()+" get"+methodName+"(){ return "+str+";}", employeeClass));
                employeeClass.addMethod(CtMethod.make("public void set"+methodName+"("+field.getType().toString()+" "+str+"){this."+str+"="+str+";}", employeeClass));
            }


            Class cl=employeeClass.toClass();

            try {
                employeeClass.writeFile(CLASS_FILE_DIRECTORY);
                employeeClass.toBytecode();
                return cl.newInstance();

            } catch (InstantiationException e) {

                LOGGER.error(e.getMessage(),e);
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage(),e);
            }
        } catch (CannotCompileException e) {

            LOGGER.error(e.getMessage(),e);
        }catch(IOException e){
            LOGGER.error(e.getMessage(),e);

        }
        return null;
    }
}
