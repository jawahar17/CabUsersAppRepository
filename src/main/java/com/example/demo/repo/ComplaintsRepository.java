package com.example.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Complaints;

public interface ComplaintsRepository extends MongoRepository<Complaints ,Integer> {
	

}
