package com.musigma.ird.socket.init;

import com.musigma.ird.socket.SocketServer;
import com.musigma.ird.socket.SocketServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sudhir
 *         Date:16/9/16
 *         Time:12:53 PM
 *         Project:SparkJava
 */
public class SocketTest {

    private static final String HOST="localhost";
    private static  final String PORT="8081";
    private  static final Logger LOGGER= LoggerFactory.getLogger(SocketTest.class);

    public static void main(String[] args) {
        testSocket();
    }
    private static void testSocket(){
        SocketServerConfig socketServerConfig=new SocketServerConfig.Config(HOST,PORT).build();
        SocketServer socketServer=socketServerConfig.getSocketServer();
        LOGGER.info("Starting socket server");
        socketServer.start();
        LOGGER.info("socket Server started");
    }
}
