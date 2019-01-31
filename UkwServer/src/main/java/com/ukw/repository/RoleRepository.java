package com.ukw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ukw.model.Role;
import com.ukw.model.RoleNames;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByRoleName(RoleNames roleName);

}
