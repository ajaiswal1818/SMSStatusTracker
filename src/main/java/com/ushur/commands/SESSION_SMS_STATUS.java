package com.ushur.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	static Logger logger = LogManager.getLogger(SESSION_SMS_STATUS.class.getName());
	
	@Override
	public JsonObject respond(JsonObject payload, Environment env, MongoClient mongoClient) {
		
		JsonObject responseObj = new JsonObject();
		BasicDBObject query = new BasicDBObject();
		try {
			responseObj.addProperty("command",payload.get("command").getAsString());
			responseObj.addProperty("SessionId", payload.get("SessionId").getAsString() );
			
			query.put( "SessionId" , payload.get("SessionId").getAsString() );
			
		} catch (Exception e) {
			logger.error(e);
			logger.info("Payload doesn't contain the required fields");
		}
		
			
		MongoDatabase db;
		try {
			db = mongoClient.getDatabase(env.getProperty("spring.data.mongodb.database"));
			
			try {
				MongoCollection<Document> collection = db.getCollection(env.getProperty("spring.data.mongodb.collection"));
				
				FindIterable<Document> findIterable;
				JsonArray doc_Array = new JsonArray();
				
				try {
					findIterable = collection.find (query);
					
					MongoCursor<Document> cursor = findIterable.iterator();
					
					while(cursor.hasNext()) {
					    
					    Document doc = cursor.next();			   

					    JsonObject curr = new JsonObject();
					    try {
							curr.addProperty("UshurMsgId", doc.get("UshurMsgId").toString());
							curr.addProperty("DateCreated",  doc.get("DateCreated").toString());
							curr.addProperty("Status", (String) doc.get("Status"));
							curr.addProperty("PartialText", (String) doc.get("PartialText"));
							curr.addProperty("Vendor", (String) doc.get("Vendor"));
							curr.addProperty("ToPhNo", (String) doc.get("ToPhNo"));
							curr.addProperty("VirtNo", (String) doc.get("VirtNo"));
						} catch (Exception e) {
							logger.error(e);
							logger.info(doc.toString() + " doesn't contain some required properties");
						}
					    
					    doc_Array.add(curr);
					    
					}
					
					
				} catch (Exception e) {
					logger.error(e);
					logger.info(query + " NOT FOUND ");
				}					
				
				logger.info("ResponseData is added to the Response Object");
				responseObj.add("responseData", doc_Array);		
				
			} catch (Exception e) {
				logger.error(e);
				logger.info("Env doesn't contain a collection.name property");
			}		
			
			
		} catch (Exception e) {
			logger.error(e);
			logger.info("Env doesn't contain a database.name property");
		}
		
		return responseObj;
	}
}