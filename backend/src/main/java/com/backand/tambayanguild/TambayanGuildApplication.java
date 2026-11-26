package com.backand.tambayanguild;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TambayanGuildApplication {

    @org.springframework.context.annotation.Bean
    org.springframework.boot.CommandLineRunner seedRewards(com.backand.tambayanguild.service.RewardService rewards) {
        return args -> rewards.seed();
    }

	public static void main(String[] args) {
		SpringApplication.run(TambayanGuildApplication.class, args);
	}

}
