package com.example.es;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAsync
@EnableScheduling
@ComponentScan("com.example.es.*")
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class EsApplication {

    public static void main(String[] args) {

        SpringApplication.run(EsApplication.class, args);
    }

}
