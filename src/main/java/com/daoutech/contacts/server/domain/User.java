package com.daoutech.contacts.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class User implements Serializable {

	public User(Integer id) {
		this.id = id;
	}

	@Id
	@GeneratedValue
	private Integer id;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Contact> contacts = new ArrayList<>();
}
