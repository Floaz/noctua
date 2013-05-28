/**
 * This file is part of Noctua.
 *
 * Copyright (C) 2013 Philipp Rene Thomas <info@noctuasource.net>
 *
 * Noctua is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Noctua is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Noctua.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.noctuasource.noctua.core.instancehost;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;


public class InstanceHostServer extends Thread {

	//private static String SERVER_SOCKET_HOLDER_CLASS = "de.floaz.osgi.launcher.Launcher";
	//private static String SERVER_SOCKET_HOLDER_METHOD = "getServerSocket";

	private static final String SERVER_PORT_MIN_KEY = "instance.host.port.min";
	private static final String SERVER_PORT_MAX_KEY = "instance.host.port.max";
	private static final String SERVER_BACKLOG_KEY = "instance.host.backlog";


	static Logger logger = Logger.getLogger(InstanceHostServer.class);


	private ServiceTracker				handlerServiceTracker;

	private ServerSocket				serverSocket = null;

	private boolean						quit = false;



	public InstanceHostServer(BundleContext bundleContext,
									Properties properties) throws Exception {
		super(InstanceHostServer.class.getName());

		serverSocket = getServerSocket(properties);

		try {
			handlerServiceTracker =
					new ServiceTracker( bundleContext,
										 InstanceHostHandler.class.getName(),
										 null);
		} catch (Exception e) {
			logger.error("Error while setting handler service tracker!");
			throw e;
		}
	}




	@Override
	public void run() {
		try {
			while(serverSocket != null && !quit) {
				Socket socket = serverSocket.accept();

				try {
					InputStream is = socket.getInputStream();
					ObjectInputStream oos = new ObjectInputStream(is);
					String[] args = (String[]) oos.readObject();

					handlerServiceTracker.open();

					Object[] handlers =
									(Object[])
											handlerServiceTracker.getServices();

					handlerServiceTracker.close();

					if(handlers.length <= 0) {
						logger.error("No handlers are installed!");
					}

					for(Object handler : handlers) {
						InstanceHostHandler h = (InstanceHostHandler)handler;
						h.parseMessage(args);
					}
				} catch(Exception e) {
					logger.error("Error while handling client socket." + e.getMessage());
				}
			}
		} catch(SocketException e) {
		} catch(Exception e) {
			logger.error("Undefined Exception in InstanceMessageAcceptor. "
							+ e.getMessage());
		} finally {
			logger.info("Quit the InstanceHostServer run!");
		}
	}




	public int getServerPort() {
		if(serverSocket != null) {
			return serverSocket.getLocalPort();
		} else {
			throw new IllegalStateException("No host instance server socket!");
		}
	}


	private static ServerSocket getServerSocket(Properties properties)
									throws Exception {

        int portMin = Integer.parseInt(properties.getProperty(SERVER_PORT_MIN_KEY));
        int portMax = Integer.parseInt(properties.getProperty(SERVER_PORT_MAX_KEY));
        int backlog = Integer.parseInt(properties.getProperty(SERVER_BACKLOG_KEY, "10"));

        ServerSocket server = null;

        for( int currentPort = portMin;
        	 server == null && currentPort < portMax;
        	 ++currentPort)
        {
        	try {
        		// No getLocalhost(), because it is often 192.168.*.*!
        		InetAddress addr = InetAddress.getByName("127.0.0.1");

        		server = new ServerSocket(currentPort, backlog, addr);
        	} catch(IOException e) {
        		logger.info("Port " + currentPort + " is busy!");
        	}
        }

        if(server == null) {
        	throw new Exception("Could not create host instance server socket!");
        }

        return server;
	}


//	private static ServerSocket getServerSocket() {
//		try {
//			ClassLoader loader = ClassLoader.getSystemClassLoader();
//
//			logger.debug("Classloader to use: " + loader.toString());
//
//			@SuppressWarnings("rawtypes")
//			Class socketHolderClass = loader.loadClass(SERVER_SOCKET_HOLDER_CLASS);
//
//			logger.debug(SERVER_SOCKET_HOLDER_CLASS + " class found!");
//
//			@SuppressWarnings("unchecked")
//			Method socketHolderMethod =
//					socketHolderClass.getMethod(
//									SERVER_SOCKET_HOLDER_METHOD, new Class[]{});
//
//			logger.debug(SERVER_SOCKET_HOLDER_METHOD + " method found!");
//
//			return (ServerSocket)
//								socketHolderMethod.invoke(null, new Object[]{});
//		} catch(Exception e) {
//			logger.error("Can't get server socket! " + e.getMessage());
//			return null;
//		}
//	}


	public void quit() {
		if(quit) {
			return;
		}

		quit = true;

		try {
			if(serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			logger.error("Error while closing server socket.", e);
		}
	}

}
