package com.musigma.ird.socket.init;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.listener.DataListener;
import com.musigma.ird.socket.SocketServer;
import com.musigma.ird.socket.SocketServerConfig;
import com.musigma.ird.sparkjava.core.SocketCommandHadler;
import com.musigma.ird.sparkjava.core.SparkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.Serializable;
import java.util.concurrent.*;

/**
 * @author sudhir
 *         Date:16/9/16
 *         Time:12:53 PM
 *         Project:SparkJava
 */
public class SocketTest implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER= LoggerFactory.getLogger(SocketTest.class);
    private static final  String keyStrorePlassword="musigma";
    private static final Transport TRANSPORT=Transport.WEBSOCKET;
    private static final ThreadPoolExecutor executor= (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private static   String HOST;
    private static   String PORT;
    private static  InputStream INPUT_STREAM;

    /*static{
        try{
            INPUT_STREAM=new FileInputStream(new File("/muESP/mustream/security/certificates/keystore.jks"));
        }catch (IOException e){
            e.printStackTrace();
        }

    }*/
    public static void main(String[] args) {
        HOST=args[0];
        PORT=args[1];
        String appname=args[2];
        String sparkNode=args[3];
        LOGGER.info("Initializing SparkContext to MasterNode "+sparkNode);
        SparkService.intializeSparkContext(appname,sparkNode);
        LOGGER.info("Starting socket server");
        testSocket();
    }
    private static void testSocket(){
       // SSLConfig sslConfig=new SSLConfig(keyStrorePlassword,TRANSPORT,INPUT_STREAM);
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
                {
                    LOGGER.info("Message received from client " + client.getRemoteAddress() + "With data :" + data);
                    Future<String> futuretask = executor.submit(new CustomTask(data.toString()));
                    String response;
                    try {
                        response = futuretask.get();
                    } catch (InterruptedException e) {
                        LOGGER.error(e.getMessage(), e);
                        response = "Error while executing task";
                    } catch (ExecutionException e) {
                        LOGGER.error(e.getMessage(), e);
                        response = "Error while executing task";
                    }

                    client.sendEvent("message", response);
                    // ackSender.sendAckData(new HelloCommand().showCommand());
                    LOGGER.info("Acknowledgement is sent to the client");
                }
            }

        });


    }
    private static class CustomTask implements Serializable, Callable<String>{
        private static final long serialVersionUID = 1L;
        private String inputJson;

        public CustomTask(String inputJson) {
            this.inputJson = inputJson;
        }

        @Override
        public String call() throws Exception {
            LOGGER.info("Handling command for input json"+inputJson);
            return new SocketCommandHadler(inputJson).handleCommand();
        }
    }

}
