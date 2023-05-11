package com.example.demo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping("/showPatients")
	public String showPatients(Model model, @RequestParam(name = "query", required = false) String query) {
		List<Patient> patients;
		if (query == null || query.trim().isEmpty()) {
		    patients = StreamSupport.stream(patientRepository.findAll().spliterator(), false)
		                            .collect(Collectors.toList());
		} else {
		    patients = patientRepository.findByNameStartingWith(query);
		}

	    model.addAttribute("ListPatients", patients);
	    return "show_patients";
	}
	
	@GetMapping("/showNewPatientForm")
	public String showNewPatientForm(Model model) {
		Patient p = new Patient();
		model.addAttribute("newPatient", p);
		return "new_patient";
	}
	
	@PostMapping("/savePatient")
	public String saveDep (@ModelAttribute("newdep") Patient p ) {
		patientRepository.save(p);
		return "redirect:/showPatients";
	}
	
	@GetMapping("/deletePatient/{id}")
	public String deletePatient(@PathVariable(value = "id") Integer id) {
	 patientRepository.deleteById(id);
	 return "redirect:/showPatients";
	}
	
	@GetMapping("/showUpdatePatientForm/{id}")
	public String showUpdatePatientForm(@PathVariable(value = "id") Integer id, Model model) {
		Optional < Patient > optional = patientRepository.findById(id);
		Patient p = null;
		if (optional.isPresent()) {
			p = optional.get();
		} else {
			throw new RuntimeException(" Patient not found for id :: " + id);
		}
		// set department as a model attribute to pre-populate the form
		model.addAttribute("patient", p);
		return "update_patient";
	}

}
