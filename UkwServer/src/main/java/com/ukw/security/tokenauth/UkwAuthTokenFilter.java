package com.ukw.security.tokenauth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ukw.security.services.UserDetailsServiceImpl;



/**
 * Filter class authenticates the user for each request.
 * 
 * @author NR
 *
 */

@Component
public class UkwAuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private UkwTokenProvider tokenProvider;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(UkwAuthTokenFilter.class);

	/**
	 * Validates the Jwt token  
	 * validate the user with UsernamePasswordAuthenticationToken
	 * Sets the proper authentication before processing the request.
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @Param FilterChain filterChain
	 * 
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String jwt = getJwt(request);
			if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
				String username = tokenProvider.getUserNameFromJwtToken(jwt);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error("No Authentication message" + e.getMessage());
		}
		filterChain.doFilter(request, response);
	}

	
	/**
	 * To remove "Bearer" to validate the token.
	 * if no "Bearer" is present returns null do no authentication is set.
	 * @param HttpServletRequest request
	 * @return the token string without "Bearer"
	 */
	private String getJwt(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.replace("Bearer ", "");
		}

		return null;
	}

}
