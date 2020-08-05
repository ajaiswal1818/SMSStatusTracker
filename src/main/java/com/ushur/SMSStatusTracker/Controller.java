package com.ushur.SMSStatusTracker;

import org.bson.Document;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@RestController
public class Controller {
	
	@Autowired
	private Environment env;
	
	@RequestMapping(value="",method=RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> request(@RequestBody String payload_String ) {
		
		JsonObject payload = new Gson().fromJson(payload_String, JsonObject.class);
		
        ApplicationContext context = new ClassPathXmlApplicationContext("configurations.xml");
        
        CommandInterface comm = (CommandInterface)context.getBean(  payload.get("command").getAsString());
        
        JsonObject responseObj = comm.respond(payload,env);
        
//        JsonObject responseObj = payload;
        
//        System.out.println(payload.toString());//get("UshurMsgId"));
//        JsonObject responseObj = new JsonObject();
//		responseObj.addProperty("command",payload.getCommand());
//		responseObj.addProperty("fromDate", payload.getFromDate() );
//		responseObj.addProperty("toDate", payload.getToDate() );
//		responseObj.addProperty("lookUpKey", payload.getLookUpKey() );
//		
//		System.setProperty("jdk.tls.trustNameService", "true");
//
//		MongoClientURI uri = new MongoClientURI(
//		    "mongodb://" + env.getProperty("spring.data.mongodb.username") + ":" +
//		    env.getProperty("spring.data.mongodb.password") + "@" + env.getProperty("spring.data.mongodb.host1") +
//		    ":" + env.getProperty("spring.data.mongodb.port") + "," + env.getProperty("spring.data.mongodb.host2") +
//		    ":" + env.getProperty("spring.data.mongodb.port") + "," + env.getProperty("spring.data.mongodb.host3") +
//		    ":" + env.getProperty("spring.data.mongodb.port")   + "/" + env.getProperty("spring.data.mongodb.database") +
//		    "?authSource=admin&retryWrites=true&w=majority");
//		
//		try (MongoClient mongoClient = new MongoClient(uri)) {
//			MongoDatabase db = mongoClient.getDatabase(env.getProperty("spring.data.mongodb.database"));
//			
//			MongoCollection<Document> collection = db.getCollection(env.getProperty("spring.data.mongodb.collection"));
//			
//			for (Document doc : collection.find()) {
//			    System.out.println(doc);
//			    responseObj.addProperty("response", doc.toString());
//			    break;
//			}
//		}
//		
//		MongoClientOptions.Builder options = MongoClientOptions.builder();
//		options.connectTimeout(60000); // .socketKeepAlive(true);
//		
//
//		MongoClient mongoClient; //= new MongoClient();
//		mongoClient = new MongoClient(uri.toString(), options.build());
//		
//		
//		
//		MongoClient mongoClient = new MongoClient();//env.getProperty("spring.data.mongodb.host"), Integer.parseInt(env.getProperty("spring.data.mongodb.port")));
//		
//		MongoDatabase db = mongoClient.getDatabase(env.getProperty("spring.data.mongodb.database"));
//		
//		MongoCollection<Document> collection = db.getCollection(env.getProperty("spring.data.mongodb.collection"));
//		
//		for (Document doc : collection.find()) {
//		    System.out.println(doc);
//		    responseObj.addProperty("response", doc.toString());
//		    break;
//		}
		
		((ClassPathXmlApplicationContext)context).close();
        
        return new ResponseEntity<Object>( responseObj.toString(), HttpStatus.ACCEPTED);	
//		return payload.toString();
		
	}
}
