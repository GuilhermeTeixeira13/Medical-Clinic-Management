package com.example.demo;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // This means that this class is a Controller
public class MainController {
	@Autowired 
	private PatientRepository patientRepository;

	@Autowired 
	private DoctorRepository doctorRepository;
	
	@Autowired 
	private AppointmentRepository appointmentRepository;
	
	@Autowired 
	private TreatmentRepository treatmentRepository;

	@GetMapping("/")
	public String showLandingPage(Model model) {
	    return "index";
	}



}
