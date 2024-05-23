package com.example.demo;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.models.ERole;
import com.example.demo.models.RoleEntity;
import com.example.demo.models.UserEntity;
import com.example.demo.repositories.UserRepository;

@SpringBootApplication
public class PruebaJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebaJwtApplication.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	@Bean
	CommandLineRunner init() {
		return args -> {
			UserEntity userEntity = UserEntity.builder()
					.email("mario@gmail.com")
					.username("mario")
					.password(passwordEncoder.encode("1234"))
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.ADMIN.name()))
							.build()))
					.build();
			UserEntity userEntity2 = UserEntity.builder()
					.email("nacho@gmail.com")
					.username("Nacho")
					.password(passwordEncoder.encode("1234"))
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.USER.name()))
							.build()))
					.build();
			UserEntity userEntity3 = UserEntity.builder()
					.email("juan@gmail.com")
					.username("Juan")
					.password(passwordEncoder.encode("1234"))
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.INVITED.name()))
							.build()))
					.build();
			userRepository.save(userEntity);
			userRepository.save(userEntity2);
			userRepository.save(userEntity3);

		};
	}
}
