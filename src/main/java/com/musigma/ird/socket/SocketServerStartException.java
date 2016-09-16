package com.musigma.ird.socket;

/**
 * The {@code SocketServerStartException} is the exception class that represents
 * the exception that is throwen while starting the socket server.
 * 
 * @author sudhir
 *
 */
public class SocketServerStartException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SocketServerStartException(String msg, Throwable e) {
		super(msg, e);
	}

	public SocketServerStartException(String msg) {
		super(msg);
	}

}
