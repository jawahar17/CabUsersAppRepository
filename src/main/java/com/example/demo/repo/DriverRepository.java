package com.example.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Driver;

public interface DriverRepository extends MongoRepository<Driver, Long> {

	Driver findByDriverId(long driverID);
}
