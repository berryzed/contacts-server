package com.daoutech.contacts.server.service;

import com.daoutech.contacts.server.JwtTokenUtil;
import com.daoutech.contacts.server.domain.User;
import com.daoutech.contacts.server.repository.UserRepository;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenUtil tokenUtil;

//	public User getCurrentUser() {
//		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//	}

	public User getUserFromToken(String token) {
		JwtParser parser = Jwts.parserBuilder()
				.requireIssuer(tokenUtil.getIssuer())
				.requireAudience(tokenUtil.getAudience())
				.setSigningKey(tokenUtil.getSecretKey())
				.build();

		Integer userId = parser.parseClaimsJws(token).getBody().get(JwtTokenUtil.CLAIM_KEY_USERID, Integer.class);
		return new User(userId);
	}
}
