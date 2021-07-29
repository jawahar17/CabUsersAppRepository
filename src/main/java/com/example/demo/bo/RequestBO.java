package com.example.demo.bo;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.example.demo.entity.BookingRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component

public class RequestBO {

	BookingRequest details;
	LocalDate dateOfTravel;
	
}
