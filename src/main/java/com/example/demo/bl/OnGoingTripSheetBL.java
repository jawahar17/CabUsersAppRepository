package com.example.demo.bl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.bo.BookingRequestDTO;
import com.example.demo.dl.TripSheetService;

@Component
public class OnGoingTripSheetBL {

	@Autowired
	private TripSheetService service;
	
	public List<BookingRequestDTO> getOnGoingTripSheet(String employeeID) {
		
		return this.service.getTripSheet(employeeID);
	}
}
