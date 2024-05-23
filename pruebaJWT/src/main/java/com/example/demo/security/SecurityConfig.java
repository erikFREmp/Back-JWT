package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.repositories.UserRepository;
import com.example.demo.security.filters.JwtAuthentificationFilter;
import com.example.demo.security.filters.JwtAuthorizationFilter;
import com.example.demo.security.jwt.JwtUtils;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Autowired
	JwtUtils JwtUtils;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtAuthorizationFilter authorizationFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
		
		JwtAuthentificationFilter jwtAuthentificationFilter = new JwtAuthentificationFilter(JwtUtils, userRepository);
		jwtAuthentificationFilter.setAuthenticationManager(authenticationManager);
		jwtAuthentificationFilter.setFilterProcessesUrl("/login");

		return httpSecurity
				.csrf(config -> config.disable())
				.authorizeHttpRequests(auth -> {
					auth.requestMatchers("/hello").authenticated();
					auth.anyRequest().permitAll();
				})
				.sessionManagement(session -> {
					session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				})
				.addFilter(jwtAuthentificationFilter)
				.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	/*
	@Bean
	UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("mario")
				.roles()
				.password("1234").build());
		return manager;
	}*/
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManger(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder)
				.and().build();
		}
	

}
