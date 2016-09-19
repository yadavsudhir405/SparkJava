package com.musigma.ird.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musigma.ird.exception.JsonToObjectConvertionException;
import com.musigma.ird.exception.ObjectToJsonConvertionException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The {@code JsonObjectMapper} is the class that deals with json to javaObejct convertion and vice-versa.
 * @author sudhir
 *         Date:19/9/16
 *         Time:11:26 AM
 *         Project:SparkJava
 */
public class JsonObjectMapper {
    private static final ObjectMapper objectMapper=new ObjectMapper();
    private static final Logger LOGGER= LoggerFactory.getLogger(JsonObjectMapper.class);
    private JsonObjectMapper(){
        throw new AssertionError("Object instantiation is not allowed for "+JsonObjectMapper.class.getName());
    }

    /**
     * The {@code  getInstanceFromJson} method converts json string to correspoinding Java Object.
     *
     *
     * @param inputJson inputjson string that has to be converted into Java object
     * @param instanceClass Class object of corresponding java obejct that you want out of json string
     * @param <T>
     * @return javaObject that represents json string
     * @throws NullPointerException - if input json string is empty or Null
     */
    public static <T> T getInstanceFromJson(final String inputJson,Class<T> instanceClass) throws JsonToObjectConvertionException{
        doEmptyOrNullCheck(inputJson);
        try {
            return objectMapper.readValue(inputJson.getBytes(),instanceClass);
        } catch (IOException e) {
            LOGGER.error("error:while converting inputjson to java object");
            throw new JsonToObjectConvertionException(e.getMessage(),e);
        }

    }

    private static void doEmptyOrNullCheck(final String inputJson){
        if(StringUtils.isEmpty(inputJson)){
            throw new NullPointerException("InputJson is empty or null "+inputJson);
        }
    }

    /**
     * <p>
     *     The method  {@code getJsonStringFromJavaObject} converts Java object to corresponding json string.
     *
     * </p>
     * @param object Java model object
     * @param <T> any java model object
     * @return json string represting the object
     * @throws NullPointerException - if null object is passed
     */
    public static <T>String getJsonStringFromJavaObject(Object object){
        doNullCheck(object);
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("error:while converting into json String from Java Object");
            throw new ObjectToJsonConvertionException(e.getMessage(),e);
        }

    }

    private static  void doNullCheck(Object t){
        if (t==null) throw new NullPointerException("Null object is passed in the argument");
    }
}
