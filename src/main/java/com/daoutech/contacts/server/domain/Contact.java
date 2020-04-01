package com.daoutech.contacts.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
		@Index(columnList = "name, user_id"), @Index(columnList = "tel, user_id"), @Index(columnList = "email, user_id")
})
public class Contact implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "이름은 필수 입니다.")
	@Size(min = 1, max = 10, message = "이름은 10자리 이하로 입력해 주세요.")
	private String name;

	@Size(max = 14, message = "전화 번호는 14자리 이하로 입력해 주세요.")
	private String tel;

	@Email(message = "이메일 형식으로 입력해 주세요.")
	@Size(max = 100, message = "이메일 주소는 100자리 이하로 입력해 주세요.")
	private String email;

	@Size(max = 100, message = "메모는 100자리 이하로 입력해 주세요.")
	private String memo;

	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private User user;

	public void updateFields(Contact contact) {
		this.name = contact.getName();
		this.tel = contact.getTel();
		this.email = contact.getEmail();
		this.memo = contact.getMemo();
	}
}
