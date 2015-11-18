package com.cluster.counter;

/**
 * The Immutable Class CounterOperation.
 */
public final class CounterOperation {

	/**
	 * The Class Builder for Counter operation.
	 */
	public static class Builder {
		/** The operation type. */
		private OpType operationType = OpType.INCREMENT_AND_GET;

		/** The counter name. */
		private String counterName = "counter";
		
		/**
		 * Builder for CounterOperation
		 *
		 * @return the counter operation
		 */
		public CounterOperation build() {
			return new CounterOperation(this);
		}

		/**
		 * set increment operation.
		 *
		 * @return the builder
		 */
		public Builder increment() {
			this.operationType = OpType.INCREMENT_AND_GET;
			return this;
		}

		/**
		 * set decrement operation.
		 *
		 * @return the builder
		 */
		public Builder decrement() {
			this.operationType = OpType.DECREMENT_AND_GET;
			return this;
		}
		
		/**
		 * Set counter name.
		 *
		 * @param counterName the counter name
		 * @return the builder
		 */
		public Builder counterName(final String counterName) {
			this.counterName = counterName;
			return this;
		}
		
		/**
		 * Sets the op type.
		 *
		 * @param opType the op type
		 * @return the builder
		 */
		public Builder setOpType(final String opType) {
			if(OpType.INCREMENT_AND_GET.name().equals(opType)) {
				operationType = OpType.INCREMENT_AND_GET;	
			} else {
				operationType = OpType.DECREMENT_AND_GET;	
			}
			
			return this;
		}
	}

	/** The operation type. */
	private final OpType operationType;

	/** The counter name. */
	private final String counterName;
	
	/**
	 * Instantiates a new counter operation object using builder.
	 *
	 * @param builder the builder
	 */
	private CounterOperation(Builder builder) {
		this.operationType = builder.operationType;
		this.counterName = builder.counterName;
	}

	/**
	 * Gets the operation type.
	 *
	 * @return the operation type
	 */
	public OpType getOperationType() {
		return operationType;
	}

	/**
	 * Gets the counter name.
	 *
	 * @return the counterName
	 */
	public String getCounterName() {
		return counterName;
	}

	@Override
	public String toString() {
	   return "CounterOperation [counterName=" + counterName + ", opType=" + operationType + "]";
	}
}
