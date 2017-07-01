package com.cts.microservices.model;

import java.util.Date;

public class ExceptionRecord {
	
	private String name;
	private String serviceName;
	private Date timeStamp;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@Override
    public String toString() {
        return "ExceptionRecord{" +
                "name='" + name + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }

}
