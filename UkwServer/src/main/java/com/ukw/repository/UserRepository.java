package com.ukw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ukw.model.User;



// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUserName(String username);

	Boolean existsByUserName(String username);

	Boolean existsByEmail(String email);

}
