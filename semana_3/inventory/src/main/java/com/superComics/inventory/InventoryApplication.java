package com.superComics.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.superComics.inventory")
public class InventoryApplication {

	public static void main(String[] args) {

        SpringApplication.run(InventoryApplication.class, args);
	}

}
