package com.ukw.security;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import com.ukw.model.Role;
import com.ukw.model.RoleNames;
import com.ukw.model.User;
import com.ukw.security.services.UkwUser;
import com.ukw.security.tokenauth.UkwTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;


import io.jsonwebtoken.Jwt;

@RunWith(SpringRunner.class)
public class TokenproviderTest {
	
	/* @TestConfiguration
	    static class TokenProviderContextConfiguration {
	  
	        @Bean
	        public MyTokenProvider myTokenProvider() {
	            return new MyTokenProvider();
	        }
	    }*/
	
	@TestConfiguration
	static class TokenProviderConfiguration{
		@Bean
		public UkwTokenProvider myTokenProvider(){
			return new UkwTokenProvider();
		}
	}
	
	@Autowired
	UkwTokenProvider  myTokenProvider;
	
	
	
	@Test
	public void test_token_generation() throws NoSuchAlgorithmException {
		
		User user1 = new User("John", "David", "johndavid123",
				"JohnD@abc.com", "pass123");
		Set<Role> setRoles =new HashSet<>();
		Role roleadmin = new Role();
		roleadmin.setRoleName(RoleNames.ROLE_ADMIN);
		setRoles.add(roleadmin);
		user1.setRoles(setRoles);
		
		
		UserDetails userDetails = UkwUser.build(user1);
		
		 	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		 	
		 	String tokenstr = myTokenProvider.generateToken(authentication);
		 	assertThat(tokenstr).isNotNull();
		 	assertThat(myTokenProvider.validateJwtToken(tokenstr)).isTrue();
		 	assertThat(myTokenProvider.getUserNameFromJwtToken(tokenstr)).isEqualTo(user1.getUserName());
		
	}
	
	

}
