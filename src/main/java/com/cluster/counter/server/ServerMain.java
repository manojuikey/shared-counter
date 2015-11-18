package com.cluster.counter.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cluster.counter.server.ClusteredCounterServer.Signal;

/**
 * The Class ServerMain.
 */
public class ServerMain {

	/** The logger. */
	private static Logger LOGGER = Logger.getLogger(ServerMain.class.getName());

	/**
	 * The main method.
	 *
	 * @param argv the arguments
	 * @throws Exception the exception
	 */
	public static void main(String argv[]) throws Exception {
		if (argv.length != 2) {
			usage();
		}
		int listenerPort = 0;
		try {
			listenerPort = Integer.parseInt(argv[0]);
		} catch (NumberFormatException e) {
			usage();
		}
		if (listenerPort <= 0) {
			usage();
		}

		ClusteredCounterServer.startServer(listenerPort, argv[1], new Signal());
	}

	/**
	 * Usage.
	 */
	private static void usage() {
		LOGGER.log(Level.SEVERE,
				"Incorrect USAGE : please provide Zookeeper enseble address in [hostname1:port1],[hostname2:port2].... format");
		LOGGER.log(Level.SEVERE, "USAGE : java " + ServerMain.class.getCanonicalName()
				+ " <Listener Port> <[hostname1:port1],[hostname2:port2]...>");

		System.exit(-1);
	}

}
