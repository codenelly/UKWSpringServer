package com.ukw.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ukw.model.User;


/**
 * Customize UserDetails 
 * @author NR
 *
 */
public class UkwUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String firstName;
	private String lastName;
	private String userName;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;

	@JsonIgnore
	private String password;

	public UkwUser(Long id, String firstName, String lastName, String userName, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {

		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	public static UkwUser build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());
		return new UkwUser(user.getId(), user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail(),
				user.getPassword(), authorities);
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUserName() {
		return userName;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UkwUser user = (UkwUser) o;
		return Objects.equals(id, user.id);
	}

}
