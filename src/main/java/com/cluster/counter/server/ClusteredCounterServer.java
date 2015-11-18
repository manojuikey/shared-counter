package com.cluster.counter.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cluster.counter.*;
import com.cluster.counter.common.json.JSONConverter;
import com.cluster.counter.zk.CounterOperationRunner;
import com.google.gson.stream.JsonReader;

public class ClusteredCounterServer {

	private static Logger LOGGER = Logger.getLogger(ClusteredCounterServer.class.getName());

	public static class Signal {
		private boolean run = true;

		boolean shouldRun() {
			return run;
		}

		public void stop() {
			run = false;
		}
	}

	/**
	 * Starts the server on the specified listener port and connects to ZooKeeper
	 * Ensemble on the address provide
	 * 
	 * @param listenerPort
	 *            TCP port where server will listen for client request to
	 *            access counter
	 * @param zookeeperQuoram
	 *            Zookeeper ensemble address in form of
	 *            hostname1:port1,hostname2:port2....
	 * @param signal
	 *            object to send stop signal
	 * @throws IOException
	 */
	public static void startServer(final int listenerPort, final String zookeeperQuoram, final Signal signal)
			throws IOException {
		LOGGER.log(Level.INFO, "Starting the counter server on port " + listenerPort);
		final CounterOperationRunner copRunner = new CounterOperationRunner(zookeeperQuoram);
		@SuppressWarnings("resource")
		final ServerSocket listening = new ServerSocket(listenerPort);
		LOGGER.log(Level.INFO, "Server started -" + listening);

		while (signal.shouldRun()) {
			Socket socket = null;
			socket = listening.accept();

			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			reader.setLenient(true);
			final CounterOperation cop = JSONConverter.getCounter(reader);

			final int result = copRunner.execute(cop);
			out.writeBytes(Integer.toString(result));
			LOGGER.log(Level.INFO, "response sent");
		}
	}

}
