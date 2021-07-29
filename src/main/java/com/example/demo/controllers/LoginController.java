package com.example.demo.controllers;

import java.net.MalformedURLException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bl.LoginBL;
import com.example.demo.bl.LoginBLTest;
import com.example.demo.bo.EmployeeBO;
import com.example.demo.entity.Employee;
import com.example.demo.status.CustomStatus;
import com.example.demo.utils.JwtUtil;
import com.microsoft.aad.adal4j.AuthenticationException;

@RestController
@CrossOrigin("*")
public class LoginController {
	
	@Autowired
	private LoginBL loginBl;
	
	@Autowired
	private LoginBLTest loginBlTest;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping(path = "/authenticate")

	//add api keys to check the request coming from mobile 
	public ResponseEntity<Employee> validateUser(@RequestBody EmployeeBO employee, HttpServletRequest request, HttpServletResponse response) {
		try {
//			String jwt = loginBl.validateUser(employee,request, response);
			
			//for test
			String jwt = loginBlTest.validateUser(employee,request, response);

			MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
			headerMap.put("Authorization", Arrays.asList(jwt));

			Employee emp = loginBl.getEmployeeDetails(employee.getEmployeeMail());
			
			return new ResponseEntity<>(emp, headerMap, HttpStatus.OK);

		} catch(BadCredentialsException | UsernameNotFoundException | AuthenticationException | ExecutionException e) {
			return ResponseEntity.status(CustomStatus.BADCREDENTIALS).body(null);
		
		} catch(LockedException e) {
			return ResponseEntity.status(CustomStatus.USERBLOCKED).body(null);
		
		} catch (MalformedURLException | InterruptedException e) {
			//log
		}
		
		return null;
	}
	
	//for testing only
	@GetMapping(path = "/empDetails")
	public ResponseEntity<Employee> getEmployeeDetails(HttpServletRequest request, Principal principal) {
		
		String jwt = request.getHeader("Authorization");
		String userName = jwtUtil.extractUserName(jwt.substring(7));
		
		Employee emp = this.loginBl.getEmployeeDetails(userName);
		return ResponseEntity.ok(emp);
	}
	
	@GetMapping("/admins")
	public List<Employee> getAdmins() {
		return loginBl.getAdmins();
	}
	
}
