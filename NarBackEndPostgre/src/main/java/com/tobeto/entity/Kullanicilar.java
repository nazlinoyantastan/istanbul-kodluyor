package com.tobeto.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQuery;

/**
 * The persistent class for the kullanicilar database table.
 * 
 */
@Entity
@NamedQuery(name = "Kullanicilar.findAll", query = "SELECT k FROM Kullanicilar k")
public class Kullanicilar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "kullanici_adi")
	private String kullaniciAdi;

	private String sifre;

	// bi-directional many-to-many association to Roller
	@ManyToMany(mappedBy = "kullanicilars")
	private List<Roller> rollers;

	public Kullanicilar() {
	}

	public UUID getId() {
		return this.id;
	}

	// deneme git commit

	public void setId(UUID id) {
		this.id = id;
	}

	public String getKullaniciAdi() {
		return this.kullaniciAdi;
	}

	public void setKullaniciAdi(String kullaniciAdi) {
		this.kullaniciAdi = kullaniciAdi;
	}

	public String getSifre() {
		return this.sifre;
	}

	public void setSifre(String sifre) {
		this.sifre = sifre;
	}

	public List<Roller> getRollers() {
		return this.rollers;
	}

	public void setRollers(List<Roller> rollers) {
		this.rollers = rollers;
	}

}