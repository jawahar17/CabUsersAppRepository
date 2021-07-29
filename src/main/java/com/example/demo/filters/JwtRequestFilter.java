package com.example.demo.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.dl.LoginDL;
import com.example.demo.entity.Employee;
import com.example.demo.status.CustomStatus;
import com.example.demo.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * This filter works before every api endpoints called
 **/

@Component
public class JwtRequestFilter extends OncePerRequestFilter{

	@Autowired
	private LoginDL loginDl;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		
		String jwt = null;
		String userName = null;
		 
		if(request.getRequestURI().startsWith("/user")) {
			try {
				
				if(authHeader != null && authHeader.startsWith("Bearer ")) {
					jwt = authHeader.substring(7);
					userName = jwtUtil.extractUserName(jwt);
				}  
				else {
					request.getSession().invalidate();
					response.setStatus(CustomStatus.SESSIONEXPIRED);
					return;
				}
				
				if(jwtUtil.validateToken(jwt, userName)) {
					
					//check if account blocked
					Employee emp = this.loginDl.findEmployeeByMail(userName);
					if(emp.getIsBlocked() == 1) {
						request.getSession().invalidate();
						response.setStatus(CustomStatus.SESSIONEXPIRED);
						return;
					}
					
					//give authentication if token is valid
					UsernamePasswordAuthenticationToken authenticationToken 
											= new UsernamePasswordAuthenticationToken(userName, null, null);
						
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);						
					
				}

			} catch (ExpiredJwtException e) {
				//returned to login page
				request.getSession().invalidate();
				response.setStatus(CustomStatus.SESSIONEXPIRED);
				return;
			} 
 		}
		
		filterChain.doFilter(request, response);
	}
	
}
