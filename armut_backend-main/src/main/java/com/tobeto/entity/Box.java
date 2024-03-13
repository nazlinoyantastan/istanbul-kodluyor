package com.tobeto.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@Data
@ToString(exclude = "fruit")
@EqualsAndHashCode(exclude = "fruit")
public class Box implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int count;
	private int capacity;
	@ManyToOne
	private Fruit fruit;
}