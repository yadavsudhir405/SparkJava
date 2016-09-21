package com.musigma.ird.socket;

import com.corundumstudio.socketio.listener.DataListener;

/**
 * The {@code SocketServer } is the SocketServer class. 
 * @author sudhir
 *
 */
public interface SocketServer {
	/**
	 * starts the socket server. 
	 * @throws SocketServerStartException - if server is already either already started or due to
	 * runtime errors occured while starting
	 */
    void start();
	/**
	 * stops the the socket server. 
	 * @throws SocketServerStop exception - if server is either not running or got problems while stopping 
	 */
    void stop();
	/**
	 * Adds the namepace to socket server. 
	 * @param namesspace namespace withot forward slash ({@literal /}) to be added to the started socket server
	 * @return namespace - if given namespace added succefully to socket server
	 * @throws IllegalArgumentException -if namepace is empty or null
	 */
    String addNameSpace(String namesspace);
	
	/**
	 * Removes the namespace from socket server.
	 * <p>This method removes the namespace from the socket server and returns true if removed succesfully.
	 * @param namespace namespace should be without forward slash {@literal /}
	 * @return true if namespace removed successfully otherwise false
	 * @throws IllegalArgumentException - if namespace is empty or null
	 */
    boolean removeNamespace(String namespace);
	/**
	 * 
	 * <P>Adds the namespaces to the socket server.
	 * <p>namespaces should not be preceded with forward slash({@literal /})
	 * @param namespace Array of namspace to be added
	 */
    void addNamespaces(String... namespace);
	/**
	 * <p> it adds the eventListner to the running server with provided eventname
	 * @param eventName
	 */
    <T> void addEventListener(String eventName, Class<T> eventclass, DataListener<T> dataListener);

	public String sendDataToNamesapce(String namespace,String event,String data);
}
