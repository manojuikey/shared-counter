package com.cluster.counter.common.json;

import java.io.IOException;

import com.cluster.counter.CounterOperation;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 * The Utility class to handle client data to and from json format.
 */
public class JSONConverter {

	/**
	 * convert to JSON representation.
	 *
	 * @param cop the cop
	 * @return the string
	 */
	public static String toJSONObject(CounterOperation cop) {

		Gson gson = new Gson();
		final String json = gson.toJson(cop);
		return json;
	}

	/**
	 * To json object.
	 *
	 * @param counterName the counter name
	 * @param opType the op type
	 * @return the string
	 */
	public static String toJSONObject(final String counterName, final String opType) {

		final CounterOperation.Builder builder = new CounterOperation.Builder();
		final CounterOperation cop = builder.counterName(counterName).setOpType(opType).build();
		Gson gson = new Gson();
		final String json = gson.toJson(cop);
		return json;
	}

	/**
	 * To java object.
	 *
	 * @param json the json
	 * @return the counter operaion
	 */
	public static CounterOperation toJavaObject(final String json) {

		Gson gson = new Gson();
		CounterOperation obj = gson.fromJson(json, CounterOperation.class);

		return obj;
	}

	/**
	 * Construct Counter operation 
	 *
	 * @see CounterOperation
	 * @param counterName the counter name
	 * @param opType the op type
	 * @return the counter operaion
	 */
	public static CounterOperation toJavaObject(final String counterName, final String opType) {

		final CounterOperation.Builder builder = new CounterOperation.Builder();
		final CounterOperation cop = builder.counterName(counterName).setOpType(opType).build();

		return cop;
	}

	/**
	 * Gets the counter operation from json format and construct java object.
	 *
	 * @param reader the json reader
	 * @return the counter operation instance
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static CounterOperation getCounter(JsonReader reader) throws IOException {
		String counterName = null;
		String opType = null;

		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("counterName")) {
				counterName = reader.nextString();
			} else if (name.equals("operationType")) {
				opType = reader.nextString();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return toJavaObject(counterName, opType);
	}
}
