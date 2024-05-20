package com.tobeto.repository.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tobeto.entities.user.Role;
import com.tobeto.entities.user.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	Optional<User> findByEmail(String email);

	@Query("SELECT r FROM User u JOIN u.roles r WHERE u.email = :email")
	List<Role> findRolesByEmail(@Param("email") String email);

	Optional<User> findByEmailAndDeletedFalse(String email);

	@Modifying
	@Query("UPDATE User u SET u.deleted = true WHERE u.email = :email")
	void softDeleteByEmail(@Param("email") String email);

	@Query("SELECT u FROM User u WHERE u.deleted = false")
	List<User> findAllActive();
}