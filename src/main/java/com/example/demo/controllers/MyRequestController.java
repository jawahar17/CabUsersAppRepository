package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bl.MyRequestBL;
import com.example.demo.bo.RequestBO;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/user/myrequest")

public class MyRequestController {

	
	
	
	
		@Autowired
		private MyRequestBL myrequestBL;
		
		
		//For getting the BookingRequest based on EmployeeId
	    @GetMapping(path = "/employeedetails/{id}")
	    public ResponseEntity<List<RequestBO>> getEmployeeDetails(@PathVariable("id") String employeeId)
	    {
	        List<RequestBO> myRequest = this.myrequestBL.getHistoryTrips(employeeId);
	        
	        return ResponseEntity.status(HttpStatus.OK).body(myRequest);
	    
	    }
	    
	    
	    
//	    @GetMapping(path = "/tripcabinfo/{dateOfTravel}")
//		public ResponseEntity<List<TripCabInfo>> getAllDates(@PathVariable("dateOfTravel")LocalDate dateOfTravel)
//	{
//			List<TripCabInfo> details = this.myrequestBL.getDate(dateOfTravel);
//			
//		return ResponseEntity.status(HttpStatus.OK).body(details);
//		}
				
}
