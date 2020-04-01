package com.daoutech.contacts.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponse implements Serializable {

	private String accessToken;
//	private String refreshToken;
}
