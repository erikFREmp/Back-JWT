package com.example.demo.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.models.UserEntity;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.jwt.JwtUtils;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity =userRepository.findByUsername(username)
								.orElseThrow(() -> new UsernameNotFoundException("El usuario "+ username+" no existe"));
	
		Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
				.collect(Collectors.toSet());
		
		return new User(userEntity.getUsername(),
				userEntity.getPassword(),
				true,
				true,
				true,
				true,
				authorities);
	}
	
	public UserEntity findUserByToken(String Token) {
		String username = jwtUtils.getUsernameFromToken(Token);
		UserEntity userEntity =userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("El usuario "+ username+" no existe"));
		return userEntity;
		
	}

}
