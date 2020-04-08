package com.daoutech.contacts.server.controller;

import com.daoutech.contacts.server.exception.AccessDeniedException;
import com.daoutech.contacts.server.security.JwtTokenUtil;
import com.daoutech.contacts.server.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
public class TokenController {

	@Autowired
	private JwtTokenUtil tokenUtil;

	@Autowired
	private UserService userService;

	@PostMapping("/token/{userId}")
	public Map<String, String> generateToken(@PathVariable("userId") Integer userId) {
		if (!userService.existsById(userId)) {
			throw new AccessDeniedException();
		}

		Claims claims = Jwts.claims();
		claims.setSubject(String.valueOf(userId));
		claims.put(JwtTokenUtil.CLAIM_KEY_USERID, userId);
		String accessToken = tokenUtil.generate(claims);
		return Collections.singletonMap("accessToken", accessToken);
	}

	@PutMapping("/token/{refreshToken}")
	public void refreshToken(@PathVariable("refreshToken") String refreshToken) {
		log.info("refreshToken");
	}
}
