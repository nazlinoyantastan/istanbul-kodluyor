package com.tobeto.repository.user;

import java.time.LocalDateTime;
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

	Optional<User> findByEmailAndIsDeletedFalse(String email);

//	@Modifying
//	@Query("UPDATE User u SET u.isDeleted = true WHERE u.email = :email")
//	void softDeleteByEmail(@Param("email") String email);

	@Query("SELECT u FROM User u WHERE u.isDeleted = false")
	List<User> findAllActive();

	@Modifying
	@Query("UPDATE User u SET u.isDeleted = true, u.deletedAt = :deletedAt WHERE u.email = :email")
	void softDeleteByEmail(@Param("email") String email, @Param("deletedAt") LocalDateTime deletedAt);

	@Modifying
	@Query("DELETE FROM User u WHERE u.isDeleted = true AND u.deletedAt <= :timestamp")
	void deleteOldSoftDeletedUsers(@Param("timestamp") LocalDateTime timestamp);
}