package com.musigma.ird.exception;

/**
 * @author sudhir
 *         Date:19/9/16
 *         Time:11:40 AM
 *         Project:SparkJava
 */
public class JsonToObjectConvertionException extends  Exception{

    public JsonToObjectConvertionException() {
    }

    /**
     * {@inheritDoc}
     * @param message
     */
    public JsonToObjectConvertionException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     * @param message
     * @param cause
     */
    public JsonToObjectConvertionException(String message, Throwable cause) {
        super(message, cause);
    }

}
