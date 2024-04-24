package com.tobeto.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tobeto.entities.user.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
