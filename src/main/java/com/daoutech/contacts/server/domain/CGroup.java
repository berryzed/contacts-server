package com.daoutech.contacts.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(
		name = "cgroup",
		indexes = {
				@Index(name = "idx_name_userid", columnList = "name, user_id")
		},
		uniqueConstraints = {
				@UniqueConstraint(name = "uc_name_userid", columnNames = {"name", "user_id"})
		}
)
public class CGroup implements Serializable {

	public CGroup() {
		this.name = "기본그룹";
	}

	public CGroup(String name) {
		this.name = name;
	}

	public CGroup(Integer id) {
		this.id = id;
	}

	@Id
	@GeneratedValue
	private Integer id;

	@Size(min = 1, max = 50, message = "그룹명은 {min}자 이상 {max}자 이하로 입력해 주세요.")
	private String name;

	@JsonIgnore
//	@JsonBackReference
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public void update(CGroup cGroup) {
		this.name = cGroup.getName();
	}
}
