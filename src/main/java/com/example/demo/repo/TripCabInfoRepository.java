package com.example.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.entity.TripCabInfo;

public interface TripCabInfoRepository extends MongoRepository<TripCabInfo, Long> {

	TripCabInfo findByTripCabId(long tripCabID);
}
