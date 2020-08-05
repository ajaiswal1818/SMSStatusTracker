package com.ushur.SMSStatusTracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ushur.SMSStatusTracker.CommandResponses.*;

@Configuration
public class CommandConfiguration {
	
	@Bean
	public SMS_STATUS sms_status() {
		return new SMS_STATUS();
	}

}
