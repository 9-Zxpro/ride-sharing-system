package me.jibajo.ride_management_service;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RideManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RideManagementServiceApplication.class, args);
	}

}
