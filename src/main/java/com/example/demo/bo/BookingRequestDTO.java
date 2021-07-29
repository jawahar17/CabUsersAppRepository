package com.example.demo.bo;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.demo.entity.Driver;
import com.example.demo.entity.TripCabInfo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequestDTO {

long bookingId;
	
	String employeeId;
	String employeeName;
	String source;
	String destination;
	String dropPoint;
	LocalTime bookingTime;
	LocalTime timeSlot;
	int addedManually;
	long tripCabId;
	LocalTime startTime;
	LocalTime reachedTime;
	String complaintDescription;
	String remarks;
	String status;
	String createdBy;
	LocalDateTime createdDate;
	String modifiedBy;
	LocalDateTime modifiedDate;
	int isDeleted;
	String slotDate;
	String bookingDate;
	
	Driver driverDetails;
	TripCabInfo cabDetails;
}
