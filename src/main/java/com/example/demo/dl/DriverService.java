package com.example.demo.dl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Driver;
import com.example.demo.repo.DriverRepository;

@Service(value = "driverService")
public class DriverService {

	@Autowired
	private DriverRepository repository;
	
	public Driver saveDriverInfo(Driver driverInfo) {
		
		return this.repository.save(driverInfo);
	}
	
	public Driver getDriverInfoByID(long driverID) {
		
		return this.repository.findByDriverId(driverID);
	}
}
