package com.ushur.SMSStatusTracker;

import org.json.JSONObject;
import org.springframework.core.env.Environment;

import com.google.gson.JsonObject;

public interface CommandInterface {
	
	JsonObject respond( JsonObject payload , Environment env);
	
}