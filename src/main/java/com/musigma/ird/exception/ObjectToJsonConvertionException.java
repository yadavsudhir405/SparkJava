package com.musigma.ird.exception;

/**
 * The {@code ObjectToJsonConvertionException} class represents the runtime exception throwen while converting to json
 * string from Java Object.
 * @author sudhir
 *         Date:19/9/16
 *         Time:12:04 PM
 *         Project:SparkJava
 */
public class ObjectToJsonConvertionException extends RuntimeException {
    /**
     * {@inheritDoc}
     * @param message
     */
    public ObjectToJsonConvertionException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     * @param message
     * @param cause
     */
    public ObjectToJsonConvertionException(String message, Throwable cause) {
        super(message, cause);
    }
}
