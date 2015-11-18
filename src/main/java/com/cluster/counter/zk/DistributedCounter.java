package com.cluster.counter.zk;

import java.nio.ByteBuffer;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

import com.cluster.counter.Counter;
import com.cluster.counter.CounterOperationException;

/**
 * The Class DistributedCounter that uses Zookeeper Curator utility.
 * 
 * @see DistributedAtomicInteger
 */
public class DistributedCounter implements Counter {

	/** The distributed counter. */
	private final DistributedAtomicInteger counter;

	/**
	 * Instantiates a new clustered counter with Persisted mode to ensure that
	 * the counter retains its state in case who whole cluster failure . Even in
	 * case of Zookeeper ensemble is restarted it will retain the state provided
	 * the zookeeper quoram is retaining the <code>data direcotry</code>
	 * contents.
	 *
	 * @param client
	 *            the zookeeper client which connects to Zookeeper ensember.
	 * @param path
	 *            the path denoting the counter name, for each counter it will
	 *            be unique path which is created by the counter name itself.
	 * @throws Exception
	 *             the exception thrown by Zookeeper client.
	 */
	public DistributedCounter(final CuratorFramework client, final String path) throws Exception {
		this.counter = new DistributedAtomicInteger(client, path, new RetryNTimes(3, 10));

		if (client.checkExists().forPath(path) == null) {
			ByteBuffer bbuffer = ByteBuffer.allocate(4);
			bbuffer.putInt(0);
			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, bbuffer.array());
		}
	}

	@Override
	public int incrementAndGet() throws CounterOperationException {
		AtomicValue<Integer> result;
		try {
			result = counter.increment();
		} catch (Exception e) {
			throw new CounterOperationException(e.getMessage());
		}
		return validateAndReturn(result);
	}

	@Override
	public int decrementAndGet() {
		AtomicValue<Integer> result;
		try {
			result = counter.decrement();
		} catch (Exception e) {
			throw new CounterOperationException(e.getMessage());
		}
		return validateAndReturn(result);
	}

	@Override
	public int getCurrentValue() {
		AtomicValue<Integer> result;
		try {
			result = counter.get();
		} catch (Exception e) {
			throw new CounterOperationException(e.getMessage());
		}
		return validateAndReturn(result);
	}

	@Override
	public void reset() {
		try {
			counter.forceSet(0);
		} catch (Exception e) {
			throw new CounterOperationException(
					"Get Operation failed on counter - " + counter + ", error-" + e.getMessage());
		}
	}

	private int validateAndReturn(final AtomicValue<Integer> result) {
		if (!result.succeeded()) {
			throw new CounterOperationException("Get Operation failed on counter - " + counter);
		}
		return result.postValue();
	}
}