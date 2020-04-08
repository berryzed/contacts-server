package com.daoutech.contacts.server.security;

import com.daoutech.contacts.server.exception.BearerTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
@Component
public class JwtTokenUtil {

	public static final String CLAIM_KEY_USERID = "userId";

	@Value("${app.jwt.secret}")
	private String secret;

	@Value("${app.jwt.issuer}")
	private String issuer;

	@Value("${app.jwt.audience}")
	private String audience;

	@Value("${app.jwt.expiration}")
	private int expiration; // millisecond

	private Key secretKey;
	private int expireTime;

	@PostConstruct
	protected void init() {
		secretKey = Keys.hmacShaKeyFor(secret.getBytes());
		expireTime = expiration * 1000;
	}

	private static final Pattern authorizationPattern = Pattern.compile(
			"^Bearer (?<token>[a-zA-Z0-9-._~+/]+)=*$",
			Pattern.CASE_INSENSITIVE);

	public static String resolve(HttpServletRequest request) {
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
			Matcher matcher = authorizationPattern.matcher(authorization);

			if (!matcher.matches()) {
				throw new BearerTokenException("토큰 정보가 정확하지 않습니다.");
			}

			return matcher.group("token");
		}

		throw new BearerTokenException("인증 헤더를 확인할 수 없습니다.");
	}

	public String generate(Claims claims) {
		Date now = new Date();
		return Jwts.builder().setClaims(claims)
				.setId(UUID.randomUUID().toString())
				.setIssuer(issuer)
				.setAudience(audience)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + expireTime)) // set Expire Time
				.signWith(secretKey, SignatureAlgorithm.HS256) // 암호화 알고리즘, secret값 세팅
				.compact();
	}
}
