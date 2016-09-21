package com.musigma.ird.sparkjava.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author sudhir
 *         Date:17/9/16
 *         Time:11:44 AM
 *         Prject:SparkJava
 */
public class HelloCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER= LoggerFactory.getLogger(HelloCommand.class);
    private String msg="Got request";
    public String showCommand(){
        LOGGER.info("Hello command ::"+msg);
        return "Mesage from Hello Command <"+msg+">";
    }
}
