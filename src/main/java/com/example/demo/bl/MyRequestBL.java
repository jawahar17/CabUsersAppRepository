package com.example.demo.bl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.bo.RequestBO;
import com.example.demo.dl.MyRequestDL;

@Component
public class MyRequestBL {

	@Autowired
	private MyRequestDL myrequestDL;
	
	//For getting the booking request based on EmployeeId
    
//	public List<BookingRequestBO> getEmployeeDetails(int employeeId) {
//		// TODO Auto-generated method stub
//		return this.myrequestDL.getEmployeeDetails(employeeId);
//	}
	
//	public List<TripCabInfo> getTripCabInfo(long tripCabId) {
//		// TODO Auto-generated method stub
//		return this.myrequestDL.getTripCabInfo(tripCabId);
//	}

		public List<RequestBO> getHistoryTrips(String employeeId) {
		return this.myrequestDL.getHistoryOfTrips(employeeId);
	}
	
}
