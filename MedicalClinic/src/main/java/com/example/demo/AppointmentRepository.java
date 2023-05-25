package com.example.demo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

//CRUD refers Create, Read, Update, Delete

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
	List<Appointment> findByPatientId(Long patientId);
	List<Appointment> findByDoctorId(Long doctorId);
	
	@Query("SELECT a.doctor, COUNT(a) as appointmentCount " +
		       "FROM Appointment a " +
		       "GROUP BY a.doctor " +
		       "ORDER BY appointmentCount DESC")
	List<Object[]> getNumberOfAppointmentsForEachDoctor();
	@Query("SELECT COUNT(a) as appointment_Count " +
			"FROM Appointment a " +
			"WHERE a.doctor.id = :doctorId AND a.start_time BETWEEN :startDateTime AND :endDateTime")
			Integer findAppointmentCountByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

	
}