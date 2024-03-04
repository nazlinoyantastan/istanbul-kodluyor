package com.tobeto.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQuery;

/**
 * The persistent class for the roller database table.
 * 
 */
@Entity
@NamedQuery(name = "Roller.findAll", query = "SELECT r FROM Roller r")
public class Roller implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String rol;

	// bi-directional many-to-many association to Kullanicilar
	@ManyToMany
	@JoinTable(name = "kullanici_rol", joinColumns = { @JoinColumn(name = "rol_id") }, inverseJoinColumns = {
			@JoinColumn(name = "kullanici_id") })
	private List<Kullanicilar> kullanicilars;

	public Roller() {
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public List<Kullanicilar> getKullanicilars() {
		return this.kullanicilars;
	}

	public void setKullanicilars(List<Kullanicilar> kullanicilars) {
		this.kullanicilars = kullanicilars;
	}

}