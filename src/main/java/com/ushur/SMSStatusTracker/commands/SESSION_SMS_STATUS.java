package com.ushur.SMSStatusTracker.commands;

import org.bson.Document;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.ushur.SMSStatusTracker.CommandInterface;

@Component
public class SESSION_SMS_STATUS implements CommandInterface
{
	@Override
	public JsonObject respond(JsonObject payload, Environment env, MongoClient mongoClient) {
		
		JsonObject responseObj = new JsonObject();
		responseObj.addProperty("command",payload.get("command").getAsString());
		responseObj.addProperty("SessionId", payload.get("SessionId").getAsString() );
		
//		try (MongoClient mongoClient = new MongoClient()) {
			
		MongoDatabase db = mongoClient.getDatabase(env.getProperty("spring.data.mongodb.database"));
		
		MongoCollection<Document> collection = db.getCollection(env.getProperty("spring.data.mongodb.collection"));
		
		BasicDBObject query = new BasicDBObject();
		
		query.put( "SessionId" , payload.get("SessionId").getAsString() ); // Provided payload.get(key) is not null. Otherwise throw ERROR.
		
		FindIterable<Document> findIterable = collection.find (query);
		MongoCursor<Document> cursor = findIterable.iterator();
		
		JsonArray doc_Array = new JsonArray();
		
		while(cursor.hasNext()) {
		    
		    Document doc = cursor.next();			   

		    JsonObject curr = new JsonObject();
		    curr.addProperty("UshurMsgId", doc.get("UshurMsgId").toString());
		    curr.addProperty("DateCreated",  doc.get("DateCreated").toString());
		    curr.addProperty("Status", (String) doc.get("Status"));
		    curr.addProperty("PartialText", (String) doc.get("PartialText"));
		    curr.addProperty("Vendor", (String) doc.get("Vendor"));
		    curr.addProperty("ToPhNo", (String) doc.get("ToPhNo"));
		    curr.addProperty("VirtNo", (String) doc.get("VirtNo"));
		    
		    doc_Array.add(curr);
		    
		}
		
		responseObj.add("responseData", doc_Array);		
			
//		}
		
		return responseObj;
	}
}