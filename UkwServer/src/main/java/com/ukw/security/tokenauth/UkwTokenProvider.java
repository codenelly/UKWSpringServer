package com.ukw.security.tokenauth;

import java.security.KeyPair;

import java.security.NoSuchAlgorithmException;

import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import com.ukw.security.services.UkwUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.crypto.RsaProvider;


/**
 * Generates and Validates web token using asymmetric keys.
 * 
 * @author NR
 *
 */
@Component
public class UkwTokenProvider {

	private long jwtExp = 900;
	// Long.parseLong(jwtExpiration);
	private static final Logger logger = LoggerFactory.getLogger(UkwTokenProvider.class);

	// public static final PrivateKey priKey = genKeys().getPrivate();
	// public static final PublicKey pubKey = genKeys().getPublic();

	// public static final Key key =
	// Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public static final KeyPair keyPair = RsaProvider.generateKeyPair(2048);

	public String generateToken(Authentication authentication) throws NoSuchAlgorithmException {

		UkwUser myuser = (UkwUser) authentication.getPrincipal();
		Claims claims = Jwts.claims().setSubject(myuser.getUserName());
		claims.put("role",
				myuser.getAuthorities().stream().map(sg -> sg.getAuthority()).collect(Collectors.joining("-")));
		claims.put("username",
				myuser.getUserName());

		// KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

		return Jwts.builder().setClaims(claims).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtExp * 1000)).signWith(keyPair.getPrivate())
				.compact();

	}

	public boolean validateJwtToken(String authToken) {
		try {
			//to do why public key is not working
			Jwts.parser().setSigningKey(keyPair.getPublic()).parseClaimsJws(authToken);
			return true;

		} catch (MalformedJwtException e) {

			logger.error("Invalid JWT token -> Message: {}", e);
		} catch (ExpiredJwtException e) {
			logger.error("Expired JWT token -> Message: {}", e);
		} catch (UnsupportedJwtException e) {
			logger.error("Unsupported JWT token -> Message: {}", e);
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty -> Message: {}", e);
		}

		return false;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(keyPair.getPrivate()).parseClaimsJws(token).getBody().getSubject();
	}

	public String getUserRoleFromJwtToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(keyPair.getPrivate()).parseClaimsJws(token).getBody();
		return (String) claims.get("roles");
	}

}
