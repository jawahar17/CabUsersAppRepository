package com.example.demo.dl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Employee;
import com.example.demo.repo.EmployeeRepository;

@Component
public class LoginDL {

	@Autowired
	private EmployeeRepository employeeRepo;
	
	public Employee findEmployeeByMail(String email) {
		return this.employeeRepo.findByEmployeeMail(email);
	}
	
	public Employee addEmployee(Employee employee) {
		return this.employeeRepo.save(employee);
	}
	
	public List<Employee> getAdmins() {
		return this.employeeRepo.findAllAdmins();
	}
}
