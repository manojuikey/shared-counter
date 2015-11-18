package com.cluster.counter.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cluster.counter.OpType;

/**
 * The Class ClientMain.
 */
public class ClientMain {
	
	/** The logger. */
	private static Logger LOGGER = Logger.getLogger(ClientMain.class.getName());

	/**
	 * The main method.
	 *
	 * @param argv the arguments
	 * @throws Exception the exception
	 */
	public static void main(String argv[]) throws Exception {
		if (argv.length != 4 || (!OpType.INCREMENT_AND_GET.name().equals(argv[3])
				&& !OpType.DECREMENT_AND_GET.name().equals(argv[3]))) {
			usage();

		}
		int serverPort = 0;
		try {
			serverPort = Integer.parseInt(argv[1]);
		} catch (NumberFormatException e) {
			usage();
		}
		if (serverPort <= 0) {
			usage();
		}
		ClusteredCounterClient.sendRequest(argv[0], serverPort, argv[2], argv[3]);
	}

	/**
	 * Usage.
	 */
	private static void usage() {
		LOGGER.log(Level.SEVERE,
				"USAGE : java " + ClientMain.class.getCanonicalName() + " <hostname> <port> <counterName> [ "
						+ OpType.INCREMENT_AND_GET.name() + "|" + OpType.DECREMENT_AND_GET.name() + "]");
		System.exit(-1);
	}
}
