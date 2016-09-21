package com.musigma.ird.socket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

class SocketServerManger {

	private static final Logger LOGGER = Logger.getLogger(SocketServerManger.class);

	private SocketServerManger() {
		throw new AssertionError("No SocketServerManager instances for you!");
	}

	/**
	 * Creates and returns new instance of socketioserver instance.
	 * 
	 * @param host
	 * @param port
     * @param sslConfig
	 * @return
	 * @throws IllegalArgumentException
	 *             - if either host or port is empty
	 */
	public static SocketIOServer newSocketServerInstance(final String host, final String port,
			final SSLConfig sslConfig) {
		if (StringUtils.isEmpty(host)) {
			throw new IllegalArgumentException("Host is empty" + host);
		}
		if (StringUtils.isEmpty(port)) {
			throw new IllegalArgumentException("port is Empty");
		}


		Configuration config = sslConfig == null ? initializeConfigurationWithoutSSLConfig(host, port)
				: initializeConfigurationWithSSLConfig(host, port, sslConfig);
		SocketIOServer socketIOServer = new SocketIOServer(config);
		return socketIOServer;
	}

	private static Configuration initializeConfigurationWithoutSSLConfig(final String host, final String port) {
		assert host != null;
		assert port != null;
		Configuration configuration = new Configuration();
		configuration.setHostname(host);
		configuration.setPort(Integer.valueOf(port));
		SocketConfig innerConfig = new SocketConfig();
		innerConfig.setReuseAddress(true);
		configuration.setSocketConfig(innerConfig);
		return configuration;

	}

	private static Configuration initializeConfigurationWithSSLConfig(final String host, final String port,
			SSLConfig sslConfig) {
		assert host != null;
		assert port != null;
		assert sslConfig != null;

		Configuration configuration = new Configuration();
		String keyStorePassword = sslConfig.getKeyStorePassword();

		SocketConfig innerConfig = new SocketConfig();
		innerConfig.setReuseAddress(true);

		configuration.setHostname(host);
		configuration.setPort(Integer.parseInt(port));

		configuration.setSocketConfig(innerConfig);
		configuration.setTransports(sslConfig.getTransport());
		configuration.setKeyStorePassword(keyStorePassword);
		configuration.setKeyStore(sslConfig.getKeyStroreInputstream());
		return configuration;

	}

	public static int getAvailablePort() {

		int freePort = 0;
		ServerSocket socket = null;
		while (true) {
			try {
				socket = new ServerSocket(0);
				freePort = socket.getLocalPort();
				break;
			} catch (Exception e) {
				LOGGER.error("Requested port not available. Looking for available ports...", e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					LOGGER.error("Encountered error while closing socket connection. ", e);
				}
			}
		}
		return freePort;

	}
}
