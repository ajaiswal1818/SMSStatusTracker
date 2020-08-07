package com.ushur.SMSStatusTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
public class SMSStatusTrackerController {
	
	@Autowired
	private Environment env;
	
	@RequestMapping(value="/query",method=RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> request(@RequestBody String payload_String ) {
		
		// The RequestBody is in the form of String and it is converted to JsonObject here.
		JsonObject payload = new Gson().fromJson(payload_String, JsonObject.class);
		
        ApplicationContext context = new ClassPathXmlApplicationContext("configurations.xml");
        
        CommandInterface comm = (CommandInterface)context.getBean(  payload.get("command").getAsString());
        
        JsonObject responseObj = comm.respond(payload,env);
		
		((ClassPathXmlApplicationContext)context).close();
        
        return new ResponseEntity<Object>( responseObj.toString(), HttpStatus.ACCEPTED);	
		
	}
}
