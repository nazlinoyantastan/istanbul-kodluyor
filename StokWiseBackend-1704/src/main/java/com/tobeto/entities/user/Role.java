package com.tobeto.entities.user;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Role {

	@Id
	@GeneratedValue
	private UUID id;
	private String name;

	@ManyToMany(mappedBy = "roles")
	private List<User> users;
}
