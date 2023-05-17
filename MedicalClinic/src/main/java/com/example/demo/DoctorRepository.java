package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

//CRUD refers Create, Read, Update, Delete

public interface DoctorRepository extends CrudRepository<Doctor, Integer> {
	Doctor findByEmail(String email);
	
	List<Doctor> findAll();
}
