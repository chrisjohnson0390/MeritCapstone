package com.meritamerica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.meritamerica.repos.UsersRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UsersRepository.class)
public class MeritamericaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeritamericaApplication.class, args);
	}

}
