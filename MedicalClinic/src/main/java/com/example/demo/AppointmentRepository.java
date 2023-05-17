package com.example.demo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

//CRUD refers Create, Read, Update, Delete

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
	List<Appointment> findByPatientId(Long patientId);
}