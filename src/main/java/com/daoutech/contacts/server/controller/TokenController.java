package com.daoutech.contacts.server.controller;

import com.daoutech.contacts.server.JwtTokenUtil;
import com.daoutech.contacts.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TokenController {

	@Autowired
	private JwtTokenUtil tokenUtil;

	@Autowired
	private UserService userService;

//	@PostMapping("/token/{userId}")
//	public TokenResponse generateToken(@PathVariable("userId") String userId) {
//		if (!userService.existsById(userId)) {
//			throw new AccessDeniedException();
//		}
//
//		Claims claims = Jwts.claims();
//		claims.setSubject(userId);
//		claims.put(JwtTokenUtil.CLAIM_KEY_USERID, userId);
//		String accessToken = tokenUtil.generate(claims);
//		return ResponseEntity.ok(new TokenResponse(accessToken));
//	}

	@PutMapping("/token/{refreshToken}")
	public void refreshToken(@PathVariable("refreshToken") String refreshToken) {
		log.info("refreshToken");
	}
}
