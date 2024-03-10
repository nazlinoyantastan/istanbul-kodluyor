package com.tobeto.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Data
@NamedQuery(name = "Member.findAll", query = "SELECT m FROM Member m")
@ToString(exclude = { "roles" })
@EqualsAndHashCode(exclude = { "roles" })
@Table(name = "member")
public class Member implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String email;

	private String password;

	// bi-directional many-to-one association to Role

	@OneToMany(mappedBy = "member")

	private List<Role> roles;

}