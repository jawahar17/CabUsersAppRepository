package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bl.CompletedTripBL;
import com.example.demo.bo.CompletedTripBO;
import com.example.demo.entity.BookingRequest;
import com.example.demo.entity.Complaints;
import com.example.demo.repo.BookingRepository;
import com.example.demo.status.CustomStatus;

@RestController
@RequestMapping(path = "/api/v1")
public class CompletedTripController {

	@Autowired
	CompletedTripBL complBL;
	@Autowired
	BookingRepository repo;
	
	
		// get CompletedTrip details.
	@GetMapping(path = "/completedTrip/{tripCabId}")
	public ResponseEntity<CompletedTripBO> getCompletedTrip(@PathVariable("tripCabId") long tripCabId) {
		CompletedTripBO completedTrip = this.complBL.getCompletedTrip(tripCabId);
		return ResponseEntity.status(HttpStatus.OK).body(completedTrip);
	}
	
	
		//get List of Complaints
	@GetMapping(path = "/complaints")
	public ResponseEntity<List<Complaints>> getComplaints() {
		List<Complaints> complaints = this.complBL.getComplaints();
		return ResponseEntity.status(HttpStatus.OK).body(complaints);
	}
	
	
		// update the complaints in DB
	@PutMapping(path = "/updateComplaints/{bookingId}/{complaint}")
	public ResponseEntity<BookingRequest> updateComplaints(@PathVariable("complaint") String complaintDes,
			@PathVariable("bookingId") long bookingId) {

		BookingRequest updatedComplaints = this.complBL.updateComplaints(bookingId, complaintDes);
		BookingRequest updateComplaints = this.repo.findByBookingId(bookingId);
		
		// get the complaints in DB
		String cmpDes = updateComplaints.getComplaintDescription();
		
		BookingRequest updatedCompl = null;

		if (cmpDes == null) {
			
			// send mail to User ---> Admin or HR
//			complBL.sendEmail(bookingId, complaintDes); // this line is responsible for the mail Uncomment the line to send the mail function.
			//System.out.println("hello");
			updateComplaints.setComplaintDescription(complaintDes);
			updatedCompl = repo.save(updateComplaints);

			

		} else {
		return  ResponseEntity.status(CustomStatus.ALREADY_REGISTERED).body(null);// Usage of custom status.
	}

		return ResponseEntity.status(HttpStatus.CREATED).body(updatedComplaints);
	}
}
