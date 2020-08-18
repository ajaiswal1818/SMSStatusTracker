package com.ushur.SMSStatusTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//import com.ushur.SMSStatusTrackerAdapter;

@ComponentScan(basePackages="com.ushur.*")
@SpringBootApplication
public class SmsStatusTrackerApplication {

//	@Autowired
//	SMSStatusTrackerAdapter adapter;
	
	public static void main(String[] args) {
		SpringApplication.run(SmsStatusTrackerApplication.class, args);	
		
	}

}
