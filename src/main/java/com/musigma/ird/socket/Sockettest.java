package com.musigma.ird.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;

public class Sockettest {

	public static void main(String[] args) {
		SocketServerConfig socketServerConfig = new SocketServerConfig.Config("localhost", "8978").build();
		SocketServer socketServer = socketServerConfig.getSocketServer();
		socketServer.start();
		socketServer.addNameSpace("hello");
		System.out.println("Socket Server started");
		socketServer.addEventListener("message", String.class, new DataListener<String>() {

			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				
				ackSender.sendAckData();
			}
		});
		System.out.println("Event with name message is added ");

	}


}
