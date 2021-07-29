package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bl.OnGoingTripSheetBL;
import com.example.demo.bo.BookingRequestDTO;

@RestController
@RequestMapping(path="/api/v1/")
@CrossOrigin(origins = "*")
public class OnGoingTripSheetController {

	@Autowired
	private OnGoingTripSheetBL service;
	
	@GetMapping(path = "/requests/{employeeID}")
	public List<BookingRequestDTO> getOnGoingTripSheet(@PathVariable("employeeID") String employeeID) {

		//Calls the getTripSheet() method of service layer
		return this.service.getOnGoingTripSheet(employeeID);
	}

}
