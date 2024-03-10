package com.tobeto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tobeto.entity.Member;

public interface UserRepository extends JpaRepository<Member, Integer> {

	Optional<Member> findByEmail(String email);

}
