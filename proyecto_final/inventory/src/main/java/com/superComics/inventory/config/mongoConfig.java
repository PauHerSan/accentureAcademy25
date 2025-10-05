package com.superComics.inventory.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.superComics.inventory.notifications.repository")
public class mongoConfig {
}
