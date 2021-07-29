package com.example.demo.dl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.bo.CompletedTripBO;
import com.example.demo.bo.TripSheetBO;
import com.example.demo.entity.BookingRequest;
import com.example.demo.entity.Complaints;
import com.example.demo.entity.Driver;
import com.example.demo.entity.Employee;
import com.example.demo.entity.TripCabInfo;
import com.example.demo.repo.BookingRepository;
import com.example.demo.repo.ComplaintsRepository;
import com.example.demo.repo.DriverRepository;
import com.example.demo.repo.EmployeeRepository;
import com.example.demo.repo.TripCabInfoRepository;

@Service(value = "completedTrip")
public class CompletedTripService {

	@Autowired
	BookingRepository repo;
	
	@Autowired
	TripCabInfoRepository cabInfoRepo;
	
	@Autowired
	DriverRepository driverInfoRepo;
	
	@Autowired
	ComplaintsRepository repository;
	
	@Autowired
	JavaMailSender javaMailSender;
	@Autowired
	EmployeeRepository empRepo;

	// To save the details
	public BookingRequest save(BookingRequest request) {
		return this.repo.save(request);
	}

	// CompletedTrip screen
	List<BookingRequest> BookingDetails;

	public CompletedTripBO getCompletedTrip(long tripCabId) {

		// get the tripCabId and fetch the details in it.
		TripCabInfo CompletedTrip = this.cabInfoRepo.findByTripCabId(tripCabId);

		// get the details of List of bookingRequestBO through tripCabId
		List<BookingRequest> BookingDetails = this.repo.findByTripCabId(tripCabId);

		long driverID = CompletedTrip.getDriverId();

		// get the driver details through driverId
		Driver info = this.driverInfoRepo.findByDriverId(driverID);

		CompletedTripBO cmptBo = new CompletedTripBO(); // completedTrip Obj.
		List<TripSheetBO> tpList = new ArrayList<>(); // Array for each employee.

		// set the required details to BO through the tripCabId and DriverId
		cmptBo.setDestination(CompletedTrip.getDestination());
		cmptBo.setDateOfTravel(CompletedTrip.getDateOfTravel());
		cmptBo.setTimeSlot(CompletedTrip.getTimeSlot());
		cmptBo.setDriverName(info.getDriverName());
		cmptBo.setDriverNumber(info.getDriverNumber());
		cmptBo.setSource(CompletedTrip.getSource());
		cmptBo.setCabNumber(CompletedTrip.getCabNumber());

		// for each employee
		for (BookingRequest eachRequest : BookingDetails) {
			tpList.add(new TripSheetBO(eachRequest.getEmployeeId(), eachRequest.getEmployeeName(), eachRequest.getDropPoint(), eachRequest.getReachedTime()));
		}

		
		cmptBo.setTripList(tpList);

		return cmptBo;

	}

	public List<Complaints> getComplaints() { // get all complaints
		return this.repository.findAll();
	}

	public BookingRequest updateComplaints(long bookingId, String complaintDescription) {
		return this.repo.findByBookingId(bookingId);// update the complaint description in BookingRequestBO
	}

	// Sent Email Service
	public SimpleMailMessage sendEmail(long bookId, String complDes) {
		// need to get the Email format
		SimpleMailMessage mail = new SimpleMailMessage();
		

		BookingRequest detailsEmp = repo.findByBookingId(bookId);
		long tripId = detailsEmp.getTripCabId();
		TripCabInfo info = cabInfoRepo.findById(tripId).get();
		long driverId = info.getDriverId();
		Driver info1 = driverInfoRepo.findByDriverId(driverId);
		Employee empInfo = empRepo.findById(detailsEmp.getEmployeeId()).get();

		//String mail1 = empInfo.getEmployeeMail(); // usage the employeemail  from the DB.
		//mail.setFrom(mail1);
		
		//System.out.println();
		
		mail.setTo( "kumarstunner@outlook.com");
		// mail.setTo("rohit.krish@hotmail.com");

		// as of now we encounterd this info.
		mail.setSubject("Compalints Registered By the Employee: " + detailsEmp.getEmployeeName());
		mail.setText("Complaint Description: " + complDes + "Of cab Number: " + info.getCabNumber() + "On"
				+ info.getDateOfTravel() + "Driver Name: " + info1.getDriverName());

		javaMailSender.send(mail);
		return mail;

	}

}
