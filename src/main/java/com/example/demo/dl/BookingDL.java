package com.example.demo.dl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bo.BookingRequestDTO;
import com.example.demo.entity.BookingRequest;
import com.example.demo.entity.Destination;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Source;
import com.example.demo.repo.BookingRepository;
import com.example.demo.repo.DestinationRepository;
import com.example.demo.repo.EmployeeRepository;
import com.example.demo.repo.SourceRepository;

@Service(value = "bookingService")
public class BookingDL {

	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	private DestinationRepository destinationRepo;
	
	@Autowired
	private SourceRepository sourceRepo;
	
	@Autowired
	private EmployeeRepository empRepo;
	
	//For getting Booking request
	public List<BookingRequest> getBookingRequests()
	{	
		return this.bookingRepo.findAll();
	}
	
	//For storing incoming Booking Request
	public BookingRequest storeBookingRequest(BookingRequestDTO request) throws Exception
	{
		request.setBookingId(bookingRepo.count()+1);
		request.setBookingTime(LocalTime.now());
		

		
		SimpleDateFormat sdf
        = new SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss");
		
		Date d1 = sdf.parse(request.getSlotDate());
        Date d2 = sdf.parse(request.getBookingDate());
		
		long difference_In_Time
        = d1.getTime() - d2.getTime();
		
		//System.out.println(difference_In_Time);
		
		long difference_In_Hours
        = (difference_In_Time
           / (1000 * 60 * 60))
          % 24;
		
		 long difference_In_Minutes
         = (difference_In_Time
            / (1000 * 60))
           % 60;
		 

		 
		 if(difference_In_Hours<1) {
			 if(Math.abs(difference_In_Minutes)<=20) {
					
					return null;
				}
		 }
		
		
		//BookingRequest book = new BookingRequest(request.getBookingId(), request.getEmployeeId() , request.getEmployeeName(), request.getSource() , request.getDestination(), request.getDropPoint() , request.getBookingTime(), request.getTimeSlot(), 0 , 0, null, null, null, null, "booked", request.getEmployeeName(), LocalDate.now(), request.getEmployeeName(), LocalDate.now(), 0);
		
		 BookingRequest book = new BookingRequest(request.getBookingId(), request.getEmployeeId(), request.getEmployeeName(), request.getSource(), request.getDestination(), request.getDropPoint(), request.getBookingTime(), request.getTimeSlot(), 0, 0, null, null, null, null, "Booked" , request.getEmployeeName(), LocalDateTime.now(), request.getEmployeeName(), LocalDateTime.now(), 0);
		 
		BookingRequest booking =  this.bookingRepo.save(book);
		
		if(booking == null) {
			throw new Exception();
		}
		
		return booking;
		
	}
	
	//For fetching all the destinations
	public List<Destination> fetchDestinationDetails()
	{
		List<Destination> destinations = this.destinationRepo.findAll();
		
		if(destinations.isEmpty()) {
			throw new NoSuchElementException();
		}
		
		return destinations;
		
	}
	
	//For fetching all the sources
	public List<Source> fetchSourceDetails()
	{
		List<Source> sources = this.sourceRepo.findAll();
		
		if(sources.isEmpty()) {
			throw new NoSuchElementException();
		}
		return sources;
	}
	
	//For Canceling the Ride
	public BookingRequest cancelTheRide(long bookingId)
	{
		Optional<BookingRequest> found = this.bookingRepo.findById(bookingId);
		
		BookingRequest cancelReq = null;
		
		if(found.isPresent())
		{	
			cancelReq = found.get();
		
			if(cancelReq.getStatus().equals("Assigned")) {
				
				return null;
			}
			
			cancelReq.setStatus("Cancelled");
		}
		
		return this.bookingRepo.save(cancelReq);
		
	}
	
	
	//For validating Whether the user has already booked or not
	public BookingRequest validateBooking(String employeeId)
	{
		return this.bookingRepo.findBookingRequestByEmployeeId(employeeId);
		
	}

	public Employee getEmployee(String employeeId) {
		
		Optional<Employee> employee = this.empRepo.findById(employeeId);
		
		return employee.get();
	}
	
	
}
