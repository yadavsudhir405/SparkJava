package com.musigma.ird.test.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.musigma.ird.sparkjava.core.Field;
import com.musigma.ird.sparkjava.core.SocketCommands;
import com.musigma.ird.sparkjava.core.Type;
import com.musigma.ird.utils.ClassGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sudhir
 *         Date:20/9/16
 *         Time:12:16 AM
 *         Project:SparkJava
 */
public class TestAnnotation {
        //private static final String HOST="192.168.1.105";
        private static final String HOST="172.25.2.77";

        private static final String PORT="8081";
        private static final Logger logger= LoggerFactory.getLogger(TestAnnotation.class);
        private static Map<String, Socket> serverConnections = new HashMap<String, Socket>();
        public static void main(String[] args){
            startSocketClinet();
            /*testClassFiles();
            System.out.println("Sleeping for 5 seconds");
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("Wokeup");
                System.out.println("Cretaing class file  again");
                testClassFiles();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
        public static void testClassFiles(){
            Field field1=new Field(Type.INTEGR, "id");
            Field field2=new Field(Type.STRING, "name");
            Field field3=new Field(Type.STRING, "email");
            List<Field> fieldList=new ArrayList<>();
            fieldList.add(field1);
            fieldList.add(field2);
            fieldList.add(field3);
            //sparkdto.setFields(fieldList);
            Object obj=ClassGenerator.getObjectWithFields("User123",fieldList);
        }
        private static void startSocketClinet(){
            try{
                SparkDTO sparkdto=new SparkDTO();
                Field field1=new Field(Type.INTEGR, "id");
                Field field2=new Field(Type.STRING, "name");
                Field field3=new Field(Type.STRING, "email");
                List<Field> fieldList=new ArrayList<>();
               // fieldList.add(field1);
                fieldList.add(field2);
                fieldList.add(field3);
                sparkdto.setFields(fieldList);

                sparkdto.setFilepath("hdfs://mastodon1.musigma.com:8020/user/musigma/vcldemo/users.json");
//sparkdto.setFileType("JSON");
                sparkdto.setQuery("select name,email from User limit 1000");
                sparkdto.setTableName("User");
                sparkdto.setSocketCommand(SocketCommands.valueOf("PERFORM_SQL_QUERY"));
                //sparkdto.setSocketCommand(SocketCommands.valueOf("LOAD_DATA_FRAME"));
//String json=new Gson().toJson(sparkdto);
//String json=JsonObjectMapper.getJsonStringFromJavaObject(sparkdto);
                ObjectMapper mapper=new ObjectMapper();
                String json=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sparkdto);
//System.out.println(json);
                sendData(HOST, json);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static void sendData(String serverIp, String data) throws Exception {

            logger.info("Requested to send data: " + data + " to server: " + serverIp);


            if (null != serverIp && !serverIp.isEmpty()) {


                if (serverConnections.containsKey(serverIp)) {

                    logger.warn("Already a connection established to this server: " + serverIp);
                    Socket socketClient = serverConnections.get(serverIp);
                    logger.warn("Sending data: " + data + " to server: " + serverIp);
                    socketClient.emit("message", data);

                } else {

                    logger.warn("Making a connection object to this server: " + serverIp);
                    Socket socketClient = makeServerConnection(serverIp);
                    logger.warn("Sending data: " + data + " to server: " + serverIp);
//Ack ack=Ack.;
                    socketClient.emit("message", data);

                }

            } else {
                throw new Exception("serverIp is null or Empty");
            }


        }
        private  static Socket makeServerConnection(final String serverIp) throws Exception {


            IO.Options op = new IO.Options();
            op.forceNew = true;
            String url = "http://" + serverIp + ":" + PORT + "/";
            final Socket socketClient = IO.socket(url);
            logger.warn("Attempting a connection with:  " + url);
            socketClient.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... objects) {
                    logger.info("Connection established with ServerIp: " + serverIp);

                }

            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    logger.error("Cannot connect to ServerIp: " + serverIp + ", problems are as below");
                    logger.error("--------------------------------------------------------------------");
                    for (Object obj : args) {
                        logger.error(obj.toString());
                    }
                    logger.error("--------------------------------------------------------------------");
/*
* Remove the Cached connection object from Map
*/

                    if (serverConnections.containsKey(serverIp)) {
                        serverConnections.get(serverIp).close();
                        serverConnections.remove(serverIp);
                    } else {

                        logger.error("You should not be seeing this case, unless there is an Socket-API BUG");

                    }


                }

            }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {

                @Override
                public void call(Object... objects) {

                    logger.error("Cannot connect (Timeout) to ServerIp: " + serverIp + ", problems are as below");
                    logger.error("--------------------------------------------------------------------");
                    for (Object obj : objects) {
                        logger.error(obj.toString());
                    }
                    logger.error("--------------------------------------------------------------------");
/*
* Remove the Cached connection object from Map
*/

                    if (serverConnections.containsKey(serverIp)) {
                        serverConnections.get(serverIp).disconnect();
                        serverConnections.remove(serverIp);
                    } else {

                        logger.error("You should not be seeing this case, unless there is an Socket-API BUG");

                    }


                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... objects) {

                    logger.error("Connection disconnected with ServerIp: " + serverIp + ", problems are as below");
                    logger.error("--------------------------------------------------------------------");
                    for (Object obj : objects) {
                        logger.error(obj.toString());
                    }
                    logger.error("--------------------------------------------------------------------");

/*
* Remove the Cached connection object from Map
*/

                    if (serverConnections.containsKey(serverIp)) {
                        serverConnections.remove(serverIp);
                    } else {

                        logger.error("You should not be seeing this case, unless there is an Socket-API BUG");

                    }

                }

            })
                    .on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
                        @Override
                        public void call(Object... objects) {
                            System.out.println("message received");
                            logger.warn("Server said as below: (but we dont know what to do with this data)");
                            logger.warn("--------------------------------------------------------------------");
                            for (Object obj : objects) {
                                logger.warn(obj.toString());
                            }
                            logger.warn("--------------------------------------------------------------------");
                            socketClient.disconnect();
                        }

                    });


            socketClient.connect();

/*
* Cache the server connection
*/
            serverConnections.put(serverIp, socketClient);

            return socketClient;

        }

    }



