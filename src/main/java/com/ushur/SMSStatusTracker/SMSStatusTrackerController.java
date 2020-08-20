package com.ushur.SMSStatusTracker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mongodb.MongoClient;


@RestController
public class SMSStatusTrackerController {
	
	@Autowired
	private Environment env;
	
	static Logger logger = LogManager.getLogger(SMSStatusTrackerController.class.getName());
	
	@RequestMapping(value="/query",method=RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> request(@RequestBody String payload_String ) {
		
		JsonObject payload ;
		JsonObject responseObj = new JsonObject();
		
		try {
			
			payload = new Gson().fromJson(payload_String, JsonObject.class);
			
			MongoClient mongoClient = new MongoClient();
	        	
        	String className = env.getProperty(payload.get("command").getAsString());
        	
        	CommandInterface comm = (CommandInterface) Class.forName(className).newInstance();
        	
        	logger.info("Bean of class " + className + " found and it's object created successfully");
	        
	        responseObj = comm.respond(payload,env, mongoClient);
				
	         
		} catch (JsonSyntaxException e1) {
			
			logger.error("JsonSyntaxException: Syntax not correct");
		
		} catch (InstantiationException e) {
		
			System.out.println("InstantiationException: Class not found");
			logger.error("InstantiationException: Class not found");
		
		} catch (IllegalAccessException e) {
		
			System.out.println("IllegalAccessException: Class not found");
			logger.error("IllegalAccessException: Class not found");
		
		} catch (ClassNotFoundException e) {
		
			System.out.println("ClassNotFoundException: Class not found");
			logger.error("ClassNotFoundException: Class not found");
		
		} catch (Exception e) {
			
			logger.error(e);
			logger.info("Either payload doesn't contain the required field or Environment doesn't contain the required property");
			
		}
        
        return new ResponseEntity<Object>( responseObj.toString(), HttpStatus.ACCEPTED);	
		
	}
}
