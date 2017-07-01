package com.cts.microservices.model;


public class ExceptionRecordSummary {

	public String name;
	public String serviceName;
	public int exceptionCounts;
	private long lastUpdatedTime;

	public static ExceptionRecordSummary fromTransaction(
			ExceptionRecord exceptionRecord) {
		ExceptionRecordSummary summary = new ExceptionRecordSummary();
		summary.name = exceptionRecord.getName();
		summary.update(exceptionRecord);
		return summary;
	}

	public void update(ExceptionRecord exceptionRecord) {
		this.exceptionCounts += 1;
		this.name = exceptionRecord.getName();
		this.serviceName = exceptionRecord.getServiceName();
		this.lastUpdatedTime = System.currentTimeMillis();
	}

	public boolean updatedWithinLastMillis(long currentTime, long limit) {
		return currentTime - this.lastUpdatedTime <= limit;
	}

}
