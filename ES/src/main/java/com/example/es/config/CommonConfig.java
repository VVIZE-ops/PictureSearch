package com.example.es.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "common")
public class CommonConfig {

    private String hosts;

    private int port;

    private String username;
}
