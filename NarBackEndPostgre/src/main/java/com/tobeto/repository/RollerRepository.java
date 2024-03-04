package com.tobeto.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tobeto.entity.Roller;

public interface RollerRepository extends JpaRepository<Roller, UUID> {

}
