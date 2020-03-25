package com.daoutech.contacts.server;

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

	@Id
	@GeneratedValue
	private Integer id;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Contact> contacts = new ArrayList<>();
}
