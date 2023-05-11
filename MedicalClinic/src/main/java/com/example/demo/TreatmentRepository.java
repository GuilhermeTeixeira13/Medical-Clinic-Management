package com.example.demo;

import org.springframework.data.repository.CrudRepository;

//CRUD refers Create, Read, Update, Delete

public interface TreatmentRepository extends CrudRepository<Treatment, Integer> {
	
}
