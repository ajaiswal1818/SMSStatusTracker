package com.ushur.SMSStatusTracker;

public class Payload {
	private String command;
	private String fromDate;
	private String toDate;
	private String lookUpKey;
	
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getLookUpKey() {
		return lookUpKey;
	}
	public void setLookUpKey(String lookUpKey) {
		this.lookUpKey = lookUpKey;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public Payload(String command, String fromDate, String toDate, String lookUpKey) {
		super();
		this.command = command;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.lookUpKey = lookUpKey;
	}
	
	
}
