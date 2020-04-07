package com.daoutech.contacts.server.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

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
}
