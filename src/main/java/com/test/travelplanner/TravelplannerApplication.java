package com.test.travelplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TravelplannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelplannerApplication.class, args);
    }

}
