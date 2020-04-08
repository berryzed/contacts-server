package com.daoutech.contacts.server.security;

import com.daoutech.contacts.server.domain.User;
import com.daoutech.contacts.server.exception.BearerTokenException;
import com.daoutech.contacts.server.exception.ErrorResponse;
import com.daoutech.contacts.server.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private UserService userService;
    private RequestMatcher tokenUrl = new AntPathRequestMatcher("/token/**");

    public JwtAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    //TODO 만료일자 검증 추가
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        if (tokenUrl.matches(req)) {
            chain.doFilter(req, res);
            return;
        }

        try {
            // 토큰 발급과 관련된 URI만 접근가능
            String token = JwtTokenUtil.resolve(req);
            User user = userService.getUserFromToken(token);

            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(req, res);
        } catch (JwtException jwtE) {
            sendError(new BearerTokenException("토큰 정보가 정확하지 않습니다."), res);
        } catch (Exception e) {
            sendError(e, res);
        }
    }

    private void sendError(Exception e, HttpServletResponse res) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(e);
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(errorResponse);

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.getWriter().write(json);
    }
}
