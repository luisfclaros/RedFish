package com.RedFish.RedFish;

import org.springframework.boot.SpringApplication;

public class TestRedFishApplication {

	public static void main(String[] args) {
		SpringApplication.from(RedFishApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
