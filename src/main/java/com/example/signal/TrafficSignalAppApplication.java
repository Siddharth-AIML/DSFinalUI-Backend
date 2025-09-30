package com.example.signal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//(exclude = {DataSourceAutoConfiguration.class})

public class TrafficSignalAppApplication {


	public static void main(String[] args) {
		SpringApplication.run(TrafficSignalAppApplication.class, args);
	}

}
