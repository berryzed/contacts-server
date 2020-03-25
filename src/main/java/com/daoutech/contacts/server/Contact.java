package com.daoutech.contacts.server;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts", indexes = {
		@Index(columnList = "name"), @Index(columnList = "tel"), @Index(columnList = "email")
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

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}
