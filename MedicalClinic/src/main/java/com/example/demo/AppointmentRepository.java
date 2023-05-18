package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

//CRUD refers Create, Read, Update, Delete

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
	List<Appointment> findByPatientId(Long patientId);
	
	@Query("SELECT a.doctor, COUNT(a) as appointmentCount " +
		       "FROM Appointment a " +
		       "GROUP BY a.doctor " +
		       "ORDER BY appointmentCount DESC")
		List<Object[]> getNumberOfAppointmentsForEachDoctor();


}