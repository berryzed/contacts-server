package com.daoutech.contacts.server.config;

import com.daoutech.contacts.server.security.JwtAuthenticationFilter;
import com.daoutech.contacts.server.security.RestAuthenticationEntryPoint;
import com.daoutech.contacts.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				.headers().frameOptions().disable() // 확인필요
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint())
				.and()
				.authorizeRequests().antMatchers("/token/**").permitAll()
				.anyRequest().authenticated()
				.and().addFilterBefore(new JwtAuthenticationFilter(userService), UsernamePasswordAuthenticationFilter.class);
	}
}
