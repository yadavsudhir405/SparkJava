package com.musigma.ird.socket.init;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.listener.DataListener;
import com.musigma.ird.socket.SSLConfig;
import com.musigma.ird.socket.SocketServer;
import com.musigma.ird.socket.SocketServerConfig;
import com.musigma.ird.sparkjava.core.HelloCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

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
    private static final  String keyStrorePlassword="musigma";
    private  static final Transport TRANSPORT=Transport.WEBSOCKET;
    private static  InputStream INPUT_STREAM;
    static{
        try{
            INPUT_STREAM=new FileInputStream(new File("/muESP/mustream/security/certificates/keystore.jks"));
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        testSocket();
    }
    private static void testSocket(){
        SSLConfig sslConfig=new SSLConfig(keyStrorePlassword,TRANSPORT,INPUT_STREAM);
        //SocketServerConfig socketServerConfig=new SocketServerConfig.Config(HOST,PORT).addSSLConfiguration(keyStrorePlassword,TRANSPORT,INPUT_STREAM).build();
        SocketServerConfig socketServerConfig=new SocketServerConfig.Config(HOST,PORT).build();
        SocketServer socketServer=socketServerConfig.getSocketServer();
        LOGGER.info("Starting socket server");
        socketServer.start();
        LOGGER.info("Adding namespace message to Socket Server");
        socketServer.addNameSpace("message");
        LOGGER.info("message namespace added");
        LOGGER.info("socket Server started, Adding message event listener");
        socketServer.addEventListener("message", String.class, new DataListener<String>() {

            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
                LOGGER.info("Message received from client "+client.getRemoteAddress()+"With data :"+data);
                TimeUnit.SECONDS.sleep(5);
                client.sendEvent("message",new HelloCommand().showCommand());
                // ackSender.sendAckData(new HelloCommand().showCommand());
                LOGGER.info("Acknowledgement is sent to the client");

            }
        });
        LOGGER.info("message event listener added");
        Thread t=new Thread( new Runnable() {
            @Override
            public void run() {
                while(true){
                    LOGGER.info(" Server brodacasting data to namespace message");
                    socketServer.sendDataToNamesapce("/message","message","Response From Socket");
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //t.start();

    }
}
