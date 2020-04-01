package com.daoutech.contacts.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

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

	@Size(min = 2, max = 16)
	@Column(unique = true, nullable = false, updatable = false)
	private String username;

	@JsonIgnore
	private String password;

	@JsonIgnore
	@Transient
	private String passwordConfirm;

	@Size(min = 2, max = 10)
	private String name;

	@Size(max = 20)
	private String tel;

	@Size(max = 100)
	private String email;

//	@JsonIgnore
//	private String accessToken; // 사용여부 판단하기

	@CreatedDate
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;

	@LastModifiedDate
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastModifiedDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastLoginDate;

	@PrePersist
	private void onPrePersist() {

	}

	@PreUpdate
	private void onPreUpdate() {

	}

//	@JsonIgnore
//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//	private List<Contact> contacts = new ArrayList<>();
}
