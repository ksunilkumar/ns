package com.nsind.widget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WidgetApiServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WidgetApiServiceApplication.class, args);
    }
}

