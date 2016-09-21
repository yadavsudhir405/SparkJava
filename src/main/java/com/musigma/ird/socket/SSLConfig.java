package com.musigma.ird.socket;

import com.corundumstudio.socketio.Transport;

import java.io.InputStream;

/**
 * {@code SSLConfig} is the class that simple represents datastructure.
 * 
 * <p>
 * This class holds properties that are needed to make Socket connection ssl
 * secure
 * 
 * @author sudhir
 *
 */
public class SSLConfig {
	private final String keyStorePassword;
	private final Transport transport;
	private final InputStream keyStroreInputstream;

	public SSLConfig(final String keyStorePassword, final Transport transport, final InputStream keyStroreInputstream) {
		this.keyStorePassword = keyStorePassword;
		this.transport = transport;
		this.keyStroreInputstream = keyStroreInputstream;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public Transport getTransport() {
		return transport;
	}

	public InputStream getKeyStroreInputstream() {
		return keyStroreInputstream;
	}
	

}