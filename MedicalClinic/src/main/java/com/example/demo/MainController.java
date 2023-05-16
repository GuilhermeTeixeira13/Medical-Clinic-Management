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

	@GetMapping("/goToLoginDoctor")
	public String goToLoginDoctor(Model model) {
	    return "goToLoginDoctor";
	}
	
	@PostMapping("/loginDoctor")
	public String loginDoctor(@RequestParam("email") String email, @RequestParam("password") String password) {
	    if (checkCredentialsDoctor(email, password)) {
	        long doctorId = doctorRepository.findByEmail(email).getDoctor_id();
	        return "redirect:/indexDoctor/" + doctorId;
	    } else {
	        return "redirect:/goToLoginDoctor";
	    }
	}
	
	public boolean checkCredentialsDoctor(String email, String password) {
        Doctor doctor = doctorRepository.findByEmail(email);
        if (doctor != null && doctor.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
	
	
	@GetMapping("/goToRegisterDoctor")
	public String goToRegisterDoctor(Model model) {
		Doctor d = new Doctor();
		model.addAttribute("new_doctor", d);
	    return "goToRegisterDoctor";
	}
	
	@PostMapping("/saveDoctor")
	public String saveDoctor (@ModelAttribute("new_doctor") Doctor d ) {
		doctorRepository.save(d);
		return "redirect:/indexDoctor";
	}
	
	@GetMapping("/indexDoctor/{doctorId}")
	public String indexDoctor(@PathVariable("doctorId") long doctorId, Model model) {
	    // Use the patientId as needed
	    model.addAttribute("doctorId", doctorId);
	    return "indexDoctor";
	}
	
	@GetMapping("/goToLoginPatient")
	public String goToLoginPatient(Model model) {
	    return "goToLoginPatient";
	}
	
	
	@PostMapping("/loginPatient")
	public String loginPatient(@RequestParam("email") String email, @RequestParam("password") String password) {
	    if (checkCredentialsPatient(email, password)) {
	        long patientId = patientRepository.findByEmail(email).getPatient_id();
	        return "redirect:/indexPatient/" + patientId;
	    } else {
	        return "redirect:/goToLoginPatient";
	    }
	}
	
	public boolean checkCredentialsPatient(String email, String password) {
        Patient patient = patientRepository.findByEmail(email);
        if (patient != null && patient.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

	
	@GetMapping("/goToRegisterPatient")
	public String goToRegisterPatient(Model model) {
		Patient p = new Patient();
		model.addAttribute("new_patient", p);
		return "goToRegisterPatient";
	}
	
	@PostMapping("/savePatient")
	public String savePatient(@ModelAttribute("new_patient") Patient p) {
	    patientRepository.save(p);
	    long patientId = p.getPatient_id(); // Assuming you have a getId() method in your Patient class
	    return "redirect:/indexPatient/" + patientId;
	}

	
	@GetMapping("/indexPatient/{patientId}")
	public String indexPatient(@PathVariable("patientId") long patientId, Model model) {
	    // Use the patientId as needed
	    model.addAttribute("patientId", patientId);
	    return "indexPatient";
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
