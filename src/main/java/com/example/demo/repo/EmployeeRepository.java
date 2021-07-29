package com.example.demo.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
	
	Employee findByEmployeeMail(String emailId);

	@Query("{isAdmin : 1}")
	List<Employee> findAllAdmins();
	
}
