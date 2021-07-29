package com.example.demo.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bl.BookingBL;
import com.example.demo.bo.BookingRequestDTO;
import com.example.demo.entity.BookingRequest;
import com.example.demo.entity.Destination;
import com.example.demo.entity.Source;
import com.example.demo.repo.BookingRepository;
import com.example.demo.status.CustomStatus;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/user/booking/")
public class BookingController {
	
	@Autowired
	private BookingBL bookingBl;

	
	//For getting Booking request
	@GetMapping(path = "/bookings")
	public ResponseEntity<List<BookingRequest>> getBookings()
	{
		List<BookingRequest> requests = this.bookingBl.getBookingRequests();
		
		return ResponseEntity.status(HttpStatus.OK).body(requests);
	}

	//For storing BookingRequest
	@PostMapping(path = "/bookacab")
	public ResponseEntity<BookingRequest> storeBookingrequest(@RequestBody BookingRequestDTO request) throws Exception
	{
		//Validating whether the user has already made a booking or not
		
		BookingRequest requestValidate = this.bookingBl.validateBooking(request.getEmployeeId());
		if(requestValidate!=null) 
		{
			if(requestValidate.getStatus().equalsIgnoreCase("booked"))
			{
				throw new Exception("Already booked");
			}
			else if(requestValidate.getStatus().equalsIgnoreCase("Inprogress") || requestValidate.getStatus().equalsIgnoreCase("Assigned"))
			{
				throw new Exception("Already Booked and cab has been assigned");
			}
		}
	
		//Storing request
		
		BookingRequest savedRequest = this.bookingBl.storeBookingRequest(request);
		
		if(savedRequest == null) {
			
			return ResponseEntity.status(CustomStatus.TIMEOUT).body(null);
		}
		
				
		return ResponseEntity.status(HttpStatus.OK).body(savedRequest);
	}
	
	//For fetching all the destinations
	@GetMapping(path = "/destinations")
	public ResponseEntity<List<Destination>> fetchDestinationDetails()
	{
		List<Destination> destinations = this.bookingBl.fetchDestinationDetails();
		
		return ResponseEntity.status(HttpStatus.OK).body(destinations);
	}
	
	//For fetching all the sources
	@GetMapping(path = "/sources")
	public ResponseEntity<List<Source>> fetchSourceDetails()
	{
		List<Source> sources = this.bookingBl.fetchSourceDetails();
		
		return ResponseEntity.status(HttpStatus.OK).body(sources);
		
	}
	
	//For Canceling the Ride
	@PutMapping(path = "/cancel/{id}")
	public ResponseEntity<BookingRequest> cancelTheRide(@PathVariable("id") long bookingId)
	{
		BookingRequest canceledReq = this.bookingBl.cancelTheRide(bookingId);
		
		if(canceledReq==null) {
			return ResponseEntity.status(CustomStatus.INPROGRESS).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(canceledReq);
		
	}
	
	
	//For validating Whether the user has already booked or not
	@GetMapping(path = "/validate/{id}")
	public ResponseEntity<BookingRequest> validateBooking(@PathVariable("id") String employeeId) throws Exception
	{
		
		BookingRequest request = this.bookingBl.validateBooking(employeeId);
		if(request!=null) 
		{
			if(request.getStatus().equalsIgnoreCase("booked"))
			{
				return ResponseEntity.status(CustomStatus.PENDING).body(request);
			}
			else if(request.getStatus().equalsIgnoreCase("Inprogress") || request.getStatus().equals("Assigned"))
			{
				return ResponseEntity.status(CustomStatus.INPROGRESS).body(request);
			}
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body(request);
	}
	@Autowired
	BookingRepository repo;
	
	@GetMapping(path = "/filters")
	public List<BookingRequest> filter(@RequestBody BookingRequest book)
	{
		//Criteria c = Criteria.where(getBookingTime()).alike(Example.of(book));
		return this.repo.findAll(Example.of(book));
	}
	
	@GetMapping(path = "/time")
	public String getBookingTime()
	{
		return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))+" "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		
	}
		
	
}
