package com.tobeto.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tobeto.entities.user.Role;
import com.tobeto.entities.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	@Query("SELECT r FROM User u JOIN u.roles r WHERE u.email = :email")
	List<Role> findRolesByEmail(@Param("email") String email);
}