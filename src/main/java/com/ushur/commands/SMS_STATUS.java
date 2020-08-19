package com.ushur.commands;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.bson.Document;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.ushur.SMSStatusTracker.CommandInterface;



@Component
public class SMS_STATUS implements CommandInterface
{
	@Override
	public JsonObject respond(JsonObject payload, Environment env, MongoClient mongoClient) {
		
		JsonObject responseObj = new JsonObject();
		responseObj.addProperty("command",payload.get("command").getAsString());
		responseObj.addProperty("fromDate", payload.get("fromDate").getAsString() );
		responseObj.addProperty("toDate", payload.get("toDate").getAsString() );
		
//		try (MongoClient mongoClient = new MongoClient()) {
			
		Duration MAX_DURATION = Duration.ofDays(Long.parseLong(env.getProperty("MAX_DURATION")));
		
		MongoDatabase db = mongoClient.getDatabase(env.getProperty("spring.data.mongodb.database"));
		
		MongoCollection<Document> collection =  db.getCollection(env.getProperty("spring.data.mongodb.collection"));
		
		BasicDBObject query = new BasicDBObject();
		
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		
		LocalDateTime fromDate = LocalDateTime.now();
		LocalDateTime toDate = LocalDateTime.now();

		if ( payload.get("fromDate").getAsString().isEmpty() && payload.get("toDate").getAsString().isEmpty() ) {
			fromDate = toDate.minus(MAX_DURATION);
		}
		else if ( payload.get("fromDate").getAsString().isEmpty() ) {
			toDate = (LocalDateTime.parse(payload.get("toDate").getAsString(),formatter));
			fromDate = toDate.minus(MAX_DURATION);
		}
		else if ( payload.get("toDate").getAsString().isEmpty()) {
			fromDate = (LocalDateTime.parse(payload.get("fromDate").getAsString(),formatter));
			toDate = fromDate.plus(MAX_DURATION);
		}
		else {
			fromDate = (LocalDateTime.parse(payload.get("fromDate").getAsString(),formatter));
			toDate = (LocalDateTime.parse(payload.get("toDate").getAsString(),formatter));
		}
		
		
		query.put("DateCreated", new BasicDBObject("$gte", fromDate).append("$lte", toDate));
	
		FindIterable<Document> findIterable = collection.find (query);
		MongoCursor<Document> cursor = findIterable.iterator();
		
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		
		
		while(cursor.hasNext()) {
		    
		    Document doc = cursor.next();
		    
		    String status = doc.getString("Status");

			map.putIfAbsent(status, 0);
			map.put( status , map.get(status) + 1 );
		    
		}
		
		JsonObject resp = new JsonObject();
		
		for(String i : map.keySet()) {
			resp.addProperty(i , map.get(i) );		
		}
		
		responseObj.add("responseData", resp);
			
//		}

		return responseObj;
	}

}