package com.daoutech.contacts.server.domain;

import lombok.*;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ScriptAssert(lang = "javascript",
		script = "(_.email != null && _.email.length() > 0) || (_.tel != null && _.tel.length() > 0)",
		alias = "_", message = "이메일 혹은 전화 번호 둘 중 하나는 필수 입니다")
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts", indexes = {
		@Index(name = "idx_name", columnList = "name"),
		@Index(name = "idx_tel", columnList = "tel"),
		@Index(name = "idx_email", columnList = "email"),
		@Index(name = "idx_name_cgroup_id", columnList = "name, cgroup_id"),
		@Index(name = "idx_tel_cgroup_id", columnList = "tel, cgroup_id"),
		@Index(name = "idx_email_cgroup_id", columnList = "email, cgroup_id")
})
public class Contact implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	@Size(min = 2, max = 20, message = "이름은 {min}자 이상 {max}자 이하로 입력해 주세요.")
	private String name;

	@Size(max = 17, message = "전화 번호는 {max}자 이하로 입력해 주세요.")
	private String tel;

	@Email(message = "이메일 형식으로 입력해 주세요.")
	@Size(max = 50, message = "이메일 주소는 {max}자 이하로 입력해 주세요.")
	private String email;

	@Size(max = 125, message = "메모는 {max}자 이하로 입력해 주세요.")
	private String memo;

	@ManyToOne(cascade = {CascadeType.DETACH})
	@JoinColumn(name = "cgroup_id", nullable = false)
	private CGroup cGroup;

	public void update(Contact contact) {
		this.name = contact.getName();
		this.tel = contact.getTel();
		this.email = contact.getEmail();
		this.memo = contact.getMemo();

		if (contact.getCGroup() != null) {
			this.cGroup.setId(contact.getCGroup().getId());
		}
	}
}
