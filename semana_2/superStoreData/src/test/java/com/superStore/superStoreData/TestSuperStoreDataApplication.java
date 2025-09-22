package com.superStore.superStoreData;

import org.springframework.boot.SpringApplication;

public class TestSuperStoreDataApplication {

	public static void main(String[] args) {
		SpringApplication.from(SuperStoreDataApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
