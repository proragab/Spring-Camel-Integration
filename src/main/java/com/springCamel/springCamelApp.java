package com.springCamel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@ComponentScan(basePackages = {"com.springCamel.*"})
@EnableResourceServer
@EnableAuthorizationServer
public class springCamelApp {

    public static void main(String[] args) {
        SpringApplication.run(springCamelApp.class, args);
    }

}
