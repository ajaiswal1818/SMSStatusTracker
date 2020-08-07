package com.ushur.SMSStatusTracker;

import org.springframework.core.env.Environment;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;

public interface CommandInterface {
	
	JsonObject respond( JsonObject payload , Environment env, MongoClient mongoClient);
	
}
