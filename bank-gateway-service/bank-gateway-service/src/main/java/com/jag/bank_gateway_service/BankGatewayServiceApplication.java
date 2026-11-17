package com.jag.bank_gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BankGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankGatewayServiceApplication.class, args);
    }

}
