package com.musigma.ird.test.socket;

import com.musigma.ird.socket.SocketServer;
import com.musigma.ird.socket.SocketServerConfig;

/**
 * @author sudhir
 *         Date:16/9/16
 *         Time:12:05 PM
 *         Project:SparkJava
 */
public class SocketTest {
    private static final String HOST="localhost";
    private static  final String PORT="8081";

    public static void main(String[] args) {
        testSocket();
    }
    private static void testSocket(){
        SocketServerConfig socketServerConfig=new SocketServerConfig.Config(HOST,PORT).build();
        SocketServer socketServer=socketServerConfig.getSocketServer();
        System.out.println("Starting socket server");
        socketServer.start();
        System.out.println("socket Server started");
    }
}
