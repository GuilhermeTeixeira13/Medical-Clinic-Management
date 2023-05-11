package com.example.demo;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

//CRUD refers Create, Read, Update, Delete

public interface PatientRepository extends CrudRepository<Patient, Integer> {
    List<Patient> findByNameStartingWith(String query);
}

