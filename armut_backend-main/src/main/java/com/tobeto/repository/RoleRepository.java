package com.tobeto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tobeto.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	List<Role> findByMemberId(int id);

}
