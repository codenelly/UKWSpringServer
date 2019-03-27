package com.ukw.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ukw.model.Role;
import com.ukw.model.RoleNames;
import com.ukw.model.User;



// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUserName(String username);

	Boolean existsByUserName(String username);

	Boolean existsByEmail(String email);
	



	//@Query( "select u from User u inner join u.roles r where r.role.roleName in :roleName" )
	@Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName=:roleName")
	List<User> findBySpecificRole(@Param("roleName") RoleNames roleName);

}
