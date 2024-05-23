package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.UserEntity;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.service.UserDetailsServiceImpl;

import org.springframework.web.bind.annotation.RequestHeader;
@CrossOrigin(originPatterns = "*")
@RestController
public class TestRolesController {
	@Autowired
	private JwtUtils jwUtils;
	@Autowired
	private UserDetailsServiceImpl userService;
	
	@GetMapping("/accessAdmin")
	@PreAuthorize("hasRole('ADMIN')")
	public String accessAdmin() {
		return("Hola, has accedido a rol de admin");
	}
	
	@GetMapping("/accessUser")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public String accessUser() {
		return("Hola, has accedido a rol de admin");
	}
	
	@GetMapping("/accessInvited")
	@PreAuthorize("hasRole('INVITED') or hasRole('ADMIN')")
	public String accessInvited() {
		return("Hola, has accedido a rol de admin");
	}
	
	@GetMapping("/checkToken")
	public boolean checkToken(@RequestHeader("Authorization") String authorizationHeader) {
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwUtils.isTokenValid(token);
            
        } else {
        	return false;
        }
	}
	
	@GetMapping("/user")
	public UserEntity findUserByToken(@RequestHeader("Authorization") String authorizationHeader) {
        
            String token = authorizationHeader.substring(7);
            return userService.findUserByToken(token);

	}
	

}
