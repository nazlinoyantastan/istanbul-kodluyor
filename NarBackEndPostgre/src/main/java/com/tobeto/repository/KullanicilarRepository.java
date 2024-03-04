package com.tobeto.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tobeto.entity.Kullanicilar;

public interface KullanicilarRepository extends JpaRepository<Kullanicilar, UUID> {
	Optional<Kullanicilar> findByKullaniciAdi(String adi);
}
