package com.ukw.message.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterForm {

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotBlank
	@Size(min = 3, max = 50)
	private String firstName;

	@Size(min = 3, max = 50)
	private String lastName;

	@NotBlank
	@Size(min = 3, max = 50)
	private String userName;

	@NotBlank
	@Size(max = 60)
	@Email
	private String email;

	private Set<String> roles;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

}
