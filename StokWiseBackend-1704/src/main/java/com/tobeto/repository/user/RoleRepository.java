package com.tobeto.repository.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tobeto.entities.user.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {

}
