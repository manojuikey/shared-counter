package com.cluster.counter.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cluster.counter.common.json.JSONConverter;

/**
 * The Class ClusteredCounterClient.
 */
public class ClusteredCounterClient {
	
	/** The logger. */
	private static Logger LOGGER = Logger.getLogger(ClusteredCounterClient.class.getName());

	/**
	 * Send request.
	 *
	 * @param hostName the host name
	 * @param port the port
	 * @param counterName the counter name
	 * @param opType the op type
	 * @return the string
	 * @throws UnknownHostException the unknown host exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String sendRequest(final String hostName, final int port, final String counterName,
			final String opType) throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(hostName, port);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));

		final String json = JSONConverter.toJSONObject(counterName, opType);

		LOGGER.log(Level.INFO, "Sending to SERVER: " + json);
		outToServer.write(json.getBytes());

		final byte[] reply = new byte[1024];
		in.read(reply);

		final String returnVal = new String(reply);
		LOGGER.log(Level.INFO, "Reply from SERVER: " + returnVal);
		clientSocket.close();

		return returnVal;
	}

}