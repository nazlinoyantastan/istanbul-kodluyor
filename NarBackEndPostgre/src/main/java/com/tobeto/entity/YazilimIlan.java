package com.tobeto.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the yazilim_ilan database table.
 * 
 */
@Entity
@Table(name = "yazilim_ilan")
@NamedQuery(name = "YazilimIlan.findAll", query = "SELECT y FROM YazilimIlan y")
public class YazilimIlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "is_tanimi")
	private String isTanimi;

	private String isim;

	private String soyisim;

	private String sure;

	private BigDecimal ucret;

	@Column(name = "yazilim_dili")
	private short yazilimDili;

	public YazilimIlan() {
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getIsTanimi() {
		return this.isTanimi;
	}

	public void setIsTanimi(String isTanimi) {
		this.isTanimi = isTanimi;
	}

	public String getIsim() {
		return this.isim;
	}

	public void setIsim(String isim) {
		this.isim = isim;
	}

	public String getSoyisim() {
		return this.soyisim;
	}

	public void setSoyisim(String soyisim) {
		this.soyisim = soyisim;
	}

	public String getSure() {
		return this.sure;
	}

	public void setSure(String sure) {
		this.sure = sure;
	}

	public BigDecimal getUcret() {
		return this.ucret;
	}

	public void setUcret(BigDecimal ucret) {
		this.ucret = ucret;
	}

	public short getYazilimDili() {
		return this.yazilimDili;
	}

	public void setYazilimDili(short yazilimDili) {
		this.yazilimDili = yazilimDili;
	}

}