package com.cts.microservices.model;


public class ExceptionRecordCollector {
	private String name;
	private String serviceName;
	public int exceptionCounts;
	private long lastUpdatedTime;
	
	public ExceptionRecordCollector add(ExceptionRecord exceptionRecord){
		if(this.name == null){
			this.name = exceptionRecord.getName();
		}
		this.exceptionCounts += 1;
		this.lastUpdatedTime = System.currentTimeMillis();
		return this;
	}
	
	@Override
    public String toString() {
        return "ExceptionRecordCollector{" +
                "name=" + name +
                ", serviceName='" + serviceName + '\'' +
                ", exceptionCounts=" + exceptionCounts +
                '}';
    }

}
