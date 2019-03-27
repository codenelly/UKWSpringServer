package com.ukw.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ukw.security.services.UserDetailsServiceImpl;
import com.ukw.security.tokenauth.UkwAuthTokenFilter;
import com.ukw.security.tokenauth.UkwAuthenticationEntryPoint;
/**
* UkwWebSecurityConfig: overrides configures authorized access to protected resources.
* 
* 
* @author NR
* 
*/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UkwWebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(UkwWebSecurityConfig.class);

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	UkwAuthenticationEntryPoint myAuthenticationEntryPoint;

	@Bean
	public UkwAuthTokenFilter ukwAuthTokenFilter() {
		return new UkwAuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.info("MyWebSecurityConfig.class");
		http.cors().and().csrf().disable().authorizeRequests().antMatchers("/", "/resources/**", "/css/**","/users/login", "/users/registration")
				.permitAll().anyRequest().authenticated().and().exceptionHandling()
				.authenticationEntryPoint(myAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(ukwAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
