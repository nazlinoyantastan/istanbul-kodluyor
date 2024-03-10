package com.tobeto.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The persistent class for the kullanici database table.
 * 
 */
@Entity
@Data
@NamedQuery(name = "User.findAll", query = "SELECT k FROM Kullanici k")
@ToString(exclude = { "roles" })
@EqualsAndHashCode(exclude = { "roles" })
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String email;

	private String password;

	// bi-directional many-to-one association to Role

	@OneToMany(mappedBy = "user")

	private List<Role> roles;

}