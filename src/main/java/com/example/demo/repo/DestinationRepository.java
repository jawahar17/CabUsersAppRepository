package com.example.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Destination;

public interface DestinationRepository extends MongoRepository<Destination, Integer> {

}
