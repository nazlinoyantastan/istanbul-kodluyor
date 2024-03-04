package com.tobeto.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tobeto.entity.YazilimIlan;

public interface YazilimIlanRepository extends JpaRepository<YazilimIlan, UUID> {

}
