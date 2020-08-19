package com.ushur.SMSStatusTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;



@RestController
public class SMSStatusTrackerController {
	
	@Autowired
	private Environment env;
	
	@RequestMapping(value="/query",method=RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> request(@RequestBody String payload_String ) {
		
		// The RequestBody is in the form of String and it is converted to JsonObject here.
		JsonObject payload = new Gson().fromJson(payload_String, JsonObject.class);
		

        ApplicationContext context = new AnnotationConfigApplicationContext(SmsStatusTrackerApplication.class);
		
        JsonObject responseObj = new JsonObject(); 
        
        try ( MongoClient mongoClient = new MongoClient() ) {
        	
        	String className = env.getProperty(payload.get("command").getAsString());
        	
        	CommandInterface comm = (CommandInterface) Class.forName(className).newInstance();
        
//        	CommandInterface comm =   (CommandInterface) context.getBean(  payload.get("command").getAsString());
	        
	        responseObj = comm.respond(payload,env, mongoClient);
			
			((AbstractApplicationContext)context).close();
			
        } catch (InstantiationException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("InstantiationException: Class not found");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("IllegalAccessException:   Class not found");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("ClassNotFoundException:   Class not found");
		}
        
        return new ResponseEntity<Object>( responseObj.toString(), HttpStatus.ACCEPTED);	
		
	}
}
