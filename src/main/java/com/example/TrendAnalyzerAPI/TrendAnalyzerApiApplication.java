package com.example.TrendAnalyzerAPI;


import config.TwitterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TwitterConfig.class)
public class TrendAnalyzerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrendAnalyzerApiApplication.class, args);
	}

}
