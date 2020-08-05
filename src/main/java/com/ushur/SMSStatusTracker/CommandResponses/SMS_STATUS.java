package com.ushur.SMSStatusTracker.CommandResponses;

import java.util.HashMap;

public class SMS_STATUS {
	private String command;
	private String fromDate;
	private String toDate;
	private String lookUpKey;
	private HashMap<String,String> response;
	
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
	public HashMap<String, String> getResponse() {
		return response;
	}
	public void setResponse(HashMap<String, String> response) {
		this.response = response;
	}
	
}
