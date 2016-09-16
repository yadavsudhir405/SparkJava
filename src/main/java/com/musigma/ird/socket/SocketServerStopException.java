package com.musigma.ird.socket;

/**
 * The {@code SocketServerStopException} is the Socket server Exception class.
 * 
 * <p>
 * The {@code SocketServerStopException } class represents the Runtime exception
 * throwen while stoping the socket server
 * 
 * @author sudhir
 *
 */
public class SocketServerStopException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SocketServerStopException(String msg, Throwable e) {
		super(msg, e);
	}

	public SocketServerStopException(String msg) {
		super(msg);
	}

}
