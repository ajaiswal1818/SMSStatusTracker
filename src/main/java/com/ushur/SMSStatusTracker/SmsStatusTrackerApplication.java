package com.ushur.SMSStatusTracker;

import org.apache.logging.log4j.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SmsStatusTrackerApplication {

	static Logger logger = LogManager.getLogger(SmsStatusTrackerApplication.class.getName());
	
	public static void main(String[] args) {
		SpringApplication.run(SmsStatusTrackerApplication.class, args);	
		logger.info("The Spring Boot Application Started");
	}

}
