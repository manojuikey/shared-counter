package com.cluster.counter.zk;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.curator.framework.CuratorFramework;

import com.cluster.counter.Counter;
import com.cluster.counter.zk.DistributedCounter;

public class DistributedCounterManager {
	private final ConcurrentMap<String, DistributedCounter> COUNTERS;

	private DistributedCounterManager() {
		COUNTERS = new ConcurrentHashMap<>();
	}
	
	public Counter createOrGetCounter(CuratorFramework client, final String counterName) throws Exception {
		if (!COUNTERS.containsKey(counterName)) {
			COUNTERS.putIfAbsent(counterName, new DistributedCounter(client, "/" + counterName));
		}
		return COUNTERS.get(counterName);
	}

	public static DistributedCounterManager getInstance() {
		return Holder.INSTANCE;
	}

	private static class Holder {
		private static DistributedCounterManager INSTANCE = new DistributedCounterManager();
	}
}