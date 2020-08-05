package com.ushur.SMSStatusTracker.commands;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;

import org.apache.tomcat.util.json.JSONParser;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.ushur.SMSStatusTracker.CommandInterface;
import com.ushur.SMSStatusTracker.Payload;

@Component
public class SMS_STATUS implements CommandInterface
{
	
	
	@Override
	public JsonObject respond(JsonObject payload, Environment env) {
		
		JsonObject responseObj = new JsonObject();
		responseObj.addProperty("command",payload.get("command").getAsString());
		responseObj.addProperty("fromDate", payload.get("fromDate").getAsString() );
		responseObj.addProperty("toDate", payload.get("toDate").getAsString() );
//		responseObj.addProperty("lookUpKey", payload.getLookUpKey() );
		
		try (MongoClient mongoClient = new MongoClient()) {
			
			Duration MAX_DURATION = Duration.ofDays(Long.parseLong(env.getProperty("MAX_DURATION")));
			
			MongoDatabase db = mongoClient.getDatabase(env.getProperty("spring.data.mongodb.database"));
			
			MongoCollection<Document> collection = db.getCollection(env.getProperty("spring.data.mongodb.collection"));
			
			BasicDBObject query = new BasicDBObject();
			
			Instant fromDate = Instant.now();
			Instant toDate = Instant.now();
//			DateTime ny = new DateTime();
			if ( payload.get("fromDate").getAsString().isEmpty() && payload.get("toDate").getAsString().isEmpty() ) {
				fromDate = toDate.minus(MAX_DURATION);
			}
			else if ( payload.get("fromDate").getAsString().isEmpty() ) {
				toDate = (Instant.parse(payload.get("toDate").getAsString()));
				fromDate = toDate.minus(MAX_DURATION);
			}
			else if ( payload.get("toDate").getAsString().isEmpty()) {
				fromDate = (Instant.parse(payload.get("fromDate").getAsString()));
				toDate = fromDate.plus(MAX_DURATION);
			}
			else {
				fromDate = (Instant.parse(payload.get("fromDate").getAsString()));
				toDate = (Instant.parse(payload.get("toDate").getAsString()));
			}

			
//			Duration duration = Duration.between(fromDate, toDate);
//			System.out.println(duration);
//			int cn = 0;
			
			
			query.put("DateCreated", new BasicDBObject("$gte", fromDate).append("$lte", toDate));
		
			FindIterable<Document> findIterable = collection.find (query);
			MongoCursor<Document> cursor = findIterable.iterator();
			
			HashMap<String,Integer> map = new HashMap<String,Integer>();
			
//			long cn2 = collection.countDocuments(query);
//			Document doc2 = new Document();
			
			while(cursor.hasNext()) {
			    
			    Document doc = cursor.next();
//			    doc2 = doc;
			    String status = doc.getString("Status");
//			    System.out.println(status);
				map.putIfAbsent(status, 0);
				map.put( status , map.get(status) + 1 );
			    
			}
			
			JsonObject resp = new JsonObject();
			
			for(String i : map.keySet()) {
//				System.out.println(i);
				resp.addProperty(i , map.get(i) );		
			}
			
			responseObj.add("responseData", resp);
			
//			String tem = doc2.toJson();
//			JsonObject temp = new Gson().fromJson(tem, JsonObject.class);

//			System.out.println(temp.toString());
			
		}

		return responseObj;
	}

}