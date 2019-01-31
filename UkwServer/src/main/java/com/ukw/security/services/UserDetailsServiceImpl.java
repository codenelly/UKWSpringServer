package com.ukw.security.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ukw.model.User;
import com.ukw.repository.UserRepository;


/**
 * Customize the UserDetailsService
 * 
 * @author NR
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUserName(username).orElseThrow(
				() -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));

		return UkwUser.build(user);
	}

}
