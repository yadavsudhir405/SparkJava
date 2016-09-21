package com.musigma.ird.socket;

import com.corundumstudio.socketio.Transport;

import java.io.InputStream;

public class SocketServerConfig {
	private final String host;
	private final String port;
	private final SSLConfig configuration;

	public SocketServerConfig(final Config config) {
		this.host = config.host;
		this.port = config.port;
		this.configuration = config.configuration;

	}

	public static class Config {
		private final String host;
		private final String port;
		private SSLConfig configuration;
		private String namespace;

		public Config(final String host, final String port) {
			this.host = host;
			this.port = port;
		}

		public Config addSSLConfiguration(String keyStorepassword, Transport transport,
				InputStream keyStroreInputstream) {
			this.configuration = new SSLConfig(keyStorepassword, transport, keyStroreInputstream);
			return this;
		}

		public Config addNamespace(String namespace) {
			this.namespace = namespace;
			return this;
		}

		public SocketServerConfig build() {
			return new SocketServerConfig(this);
		}
	}
	/**
	 * 
	 * @return  SocketServer new instance of socket server instance
	 */
	public SocketServer getSocketServer() {
		return new SocketServerImpl(SocketServerManger.newSocketServerInstance(host, port, configuration));

	}

}
