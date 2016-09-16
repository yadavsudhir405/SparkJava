package com.musigma.ird.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code SocketServer} starts the socket server.
 * 
 * @author sudhir
 *
 */
class SocketServerImpl implements SocketServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketServerImpl.class);
	private SocketIOServer socketIoServer;
	private volatile boolean isServerStarted;

	public SocketServerImpl(final SocketIOServer socketIOServer) {
		this.socketIoServer = socketIOServer;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void start() {
		if (isServerStarted) {
			throw new IllegalStateException("Socket server has already been started");
		}
		try {
			socketIoServer.start();
			LOGGER.info("\n********************************************************************\n"
					+ "Socket Server started at " + socketIoServer.getConfiguration().getHostname()
					+ " with port number :" + socketIoServer.getConfiguration().getPort() + "\n"
					+ "*************************************************************************");
			socketIoServer.addConnectListener(new ConnectListener() {

				@Override
				public void onConnect(SocketIOClient client) {
					LOGGER.info("Connected to remote client " + client.getRemoteAddress());

				}
			});
		} catch (Exception e) {
			LOGGER.error("Error while starting socket server ", e);
			throw new SocketServerStartException(e.getMessage(), e);
		}

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void stop() {
		try {
			socketIoServer.stop();
		} catch (Exception e) {
			LOGGER.error("Error while stoping socket server ", e);
			throw new SocketServerStopException(e.getMessage(), e);
		}

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public String addNameSpace(final String namespace) {
		if (StringUtils.isEmpty(namespace)) {
			throw new IllegalArgumentException("Empty namespace provided " + namespace);
		}
		ensureServerRunning();
		socketIoServer.addNamespace("/" + namespace);
		return namespace;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public boolean removeNamespace(String namespace) {
		if (StringUtils.isEmpty(namespace)) {
			throw new IllegalArgumentException("Empty namespace provided " + namespace);
		}
		ensureServerRunning();
		try {
			socketIoServer.removeNamespace(namespace);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void addNamespaces(String... namespaces) {
		ensureServerRunning();
		for (String namespace : namespaces) {
			addNameSpace(namespace);
		}

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public <T> void addEventListener(String eventName, Class<T> eventClass, DataListener<T> dataListener) {
		// socketIoServer.addEventListener(eventName, eventClass, dataListener);
		ensureServerRunning();
		socketIoServer.addEventListener(eventName, eventClass, dataListener);
	}

	private void ensureServerRunning() {
		if (!isServerStarted) {
			throw new IllegalStateException("Socket Server is not up and running");
		}
	}

}
