package com.example.demo.bl;

import java.net.MalformedURLException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.bo.EmployeeBO;
import com.example.demo.dl.LoginDL;
import com.example.demo.entity.Employee;
import com.example.demo.utils.JwtUtil;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationException;
import com.microsoft.aad.adal4j.AuthenticationResult;

@Component
public class LoginBL {
	
	@Autowired
	private LoginDL loginDl;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public String validateUser(@RequestBody EmployeeBO employee, HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException, MalformedURLException {
		String jwt = null;
		
		Employee emp = this.loginDl.findEmployeeByMail(employee.getEmployeeMail());

		if(emp == null) {
			
//			RestTemplate template = new RestTemplate();
//															
//			//body, method, url , type
//			RequestEntity<Void> entity = RequestEntity.get("url").accept(MediaType.APPLICATION_JSON).header("Authorization", "Bearer").build();
//			ResponseEntity<String> spResponse = template.exchange(entity, String.class);
//			spResponse.getBody();
			
			throw new BadCredentialsException("User Not Found");
		}
		
		if(emp.getIsBlocked() == 1 && emp.getIsAdmin()!=1) {
			throw new LockedException("Account Blocked");
		}
		
		try {
			//decode the base64 encoded password
			byte[] password = Base64.getDecoder().decode(employee.getPassword());
			employee.setPassword(new String(password));

			//=============authenticate from azure ad===================================//
//			String authorityURL = "https://login.windows.net/avam365test.onmicrosoft.com";
//
//			AuthenticationContext context = new AuthenticationContext(authorityURL, false, Executors.newScheduledThreadPool(20));
//
//		    Future<AuthenticationResult> result = 
//		    		context.acquireToken("https://graph.microsoft.com", 
//		    				"cf71fee4-0e23-463e-a862-919c2d6b05da", "thane@avam365test.onmicrosoft.com", "Rov84743", null);
		    
		    Future<AuthenticationResult> result = 
		    		azureValidation(employee.getEmployeeMail(), employee.getPassword());
			
		    //generate jwt token if valid user
		    if(result.get().getAccessToken() != null) {
		    	jwt = "Bearer " + jwtUtil.generateToken(employee.getEmployeeMail());
				
				/* setting manual authentication 
				 * pass null for password and empty list for authorities since we don't use it anywhere
				 * passing 3 parameters is important!
				 * */
		    	UsernamePasswordAuthenticationToken authenticationToken 
									= new UsernamePasswordAuthenticationToken(employee.getEmployeeMail(), null, Collections.emptyList());
		    	
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				
				//session timeout for 15 mins
				request.getSession().setMaxInactiveInterval(900);
				
				return jwt;
		    }
		  //=======================ad auth ends here ===========================================//		
		    
			//this exception occurs on invalid credentials or if account is locked
		} catch (BadCredentialsException | LockedException | UsernameNotFoundException | ExecutionException | AuthenticationException e) {
			throw e;
			
		} catch (MalformedURLException | InterruptedException e) {
			throw e;

		} catch (Exception e) {
			
		}
		
		return jwt;
	}
	
	private Future<AuthenticationResult> azureValidation(String mail, String password) throws MalformedURLException, InterruptedException, ExecutionException {
		String authorityURL = "https://login.windows.net/avam365test.onmicrosoft.com";

		AuthenticationContext context = new AuthenticationContext(authorityURL, false, Executors.newScheduledThreadPool(1));

	    Future<AuthenticationResult> result = 
	    		context.acquireToken("https://graph.microsoft.com", 
					"cf71fee4-0e23-463e-a862-919c2d6b05da", mail, password, null);
	    
		result.get().getAccessToken();
		return result;
	}
	
	public Employee getEmployeeDetails(String email) {
		return this.loginDl.findEmployeeByMail(email);
	}
	
	public List<Employee> getAdmins() {
		return loginDl.getAdmins();
	}
	
}
