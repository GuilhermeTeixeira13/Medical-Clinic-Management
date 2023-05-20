package com.example.demo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

//CRUD refers Create, Read, Update, Delete

public interface TreatmentRepository extends CrudRepository<Treatment, Long> {
	List<Treatment> findByAppointmentId(Long appointment_id);
}
