package com.example.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import myinputs.Ler;

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
	        long doctorId = doctorRepository.findByEmail(email).getId();
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
		long doctorId = d.getId();
		return "redirect:/indexDoctor/" + doctorId;
	}
	
	@GetMapping("/indexDoctor/{doctorId}")
	public String indexDoctor(@PathVariable("doctorId") long doctorId, Model model) {
	    model.addAttribute("doctorId", doctorId);
	    return "indexDoctor";
	}
	
	@GetMapping("/seeAppointmentsdoctor/{doctorId}")
	public String seeAppointmentsdoctor(
	    @PathVariable("doctorId") long doctorId,
	    Model model
	) {
	    model.addAttribute("doctorId", doctorId);
	    
	    List<Appointment> appointment = appointmentRepository.findByDoctorId(doctorId);

	    model.addAttribute("ListAppointments", appointment);

	    return "seeAppointmentsdoctor";
	}
	
	@GetMapping("/seetreatments/{appointmentid}")
	public String seeTreatments(
		    @PathVariable("appointmentid") long appointmentid,
		    Model model
		) {
		    model.addAttribute("appointmentid", appointmentid);
		    
		    List<Treatment> treatments = treatmentRepository.findByAppointmentId(appointmentid);

		    model.addAttribute("Listtreatments", treatments);

		    return "seetreatments";
		}
	
	@GetMapping("/addtreatment/{appointmentid}")
	public String addtreatment(@PathVariable("appointmentid") long appointmentid,
		    Model model
			) {
		model.addAttribute("appointmentid", appointmentid);
		Treatment t = new Treatment();
		model.addAttribute("new_treatment", t);
		return "addtreatment";
	}
	
	@PostMapping("/savetreatment/{appointmentid}")
	public String saveTreatment(
	    @PathVariable("appointmentid") String appointmentid,
	    @ModelAttribute("new_treatment") Treatment newTreatment
	) {
	    long appointmentId = Long.parseLong(appointmentid);
	    Appointment appointment = getAppointmentById(appointmentId);
	    newTreatment.setAppointment(appointment);
	    treatmentRepository.save(newTreatment);

	    return "redirect:/seetreatments/" + appointmentid;
	}
	
	@GetMapping("/deleteTreatment/{id}/{appointmentid}")
	public String deleteTreatment(@PathVariable(value = "id") Long id, @PathVariable(value = "appointmentid") long appointmentid) {
		treatmentRepository.deleteById(id);
		return "redirect:/seetreatments/" + appointmentid;
	}
	
	@GetMapping("/statsdoctor/{doctorId}")
	public String statsdoctor(
			Model model,
			@PathVariable("doctorId") long doctorId
	) {
	    return "statsdoctor";
	}
	@GetMapping("/Numberofappointmentsinagivenperiodoftime/{doctorId}")
	public String getNumberofappointmentsinagivenperiodoftime(Model model, @PathVariable Long doctorId) {
	    model.addAttribute("doctorId", doctorId);
	    return "Numberofappointmentsinagivenperiodoftime";
	}
	
	@PostMapping("/search")
	public String search(Model model, @RequestParam("doctorId") Long doctorId, @RequestParam("start_time") LocalDateTime start_time,@RequestParam("end_time") LocalDateTime end_time) {
	    Integer appointmentCount = appointmentRepository.findAppointmentCountByDoctorAndDate(doctorId, start_time,end_time);
	    model.addAttribute("appointmentCount", appointmentCount);
	    model.addAttribute("doctorId", doctorId);
	    model.addAttribute("start_time", start_time);
	    model.addAttribute("end_time", end_time);
	    return "redirect:/Numberofappointmentsinagivenperiodoftimeresult/"+doctorId+"/"+appointmentCount;
	}

	@GetMapping("/Numberofappointmentsinagivenperiodoftimeresult/{doctorId}/{appointmentCount}")
	public String getNumberofappointmentsinagivenperiodoftimeresult(Model model, @PathVariable Long doctorId, @PathVariable Integer appointmentCount) {
	    model.addAttribute("doctorId", doctorId);
	    model.addAttribute("appointmentCount", appointmentCount);
	    return "Numberofappointmentsinagivenperiodoftimeresult";
	}
	
	@GetMapping("/Numberofappointmentsforagivenday/{doctorId}")
	public String getNumberofappointmentsforagivenday(Model model, @PathVariable Long doctorId) {
	    model.addAttribute("doctorId", doctorId);
	    return "Numberofappointmentsforagivenday";
	}
	
	@PostMapping("/search1")
	public String search1(Model model, @RequestParam("doctorId") Long doctorId, @RequestParam("start_time") LocalDateTime start_time) {
	    Integer appointmentCount = appointmentRepository.findAppointmentCountByDoctorAndDate(doctorId, start_time.withHour(0).withMinute(0).withSecond(0).withNano(0),start_time.withHour(23).withMinute(59).withSecond(59).withNano(999999999));
	    model.addAttribute("appointmentCount", appointmentCount);
	    model.addAttribute("doctorId", doctorId);
	    model.addAttribute("start_time", start_time);
	    return "redirect:/Numberofappointmentsforagivendayresult/"+doctorId+"/"+appointmentCount;
	}

	@GetMapping("/Numberofappointmentsforagivendayresult/{doctorId}/{appointmentCount}")
	public String getNumberofappointmentsforagivendayresult(Model model, @PathVariable Long doctorId, @PathVariable Integer appointmentCount) {
	    model.addAttribute("doctorId", doctorId);
	    model.addAttribute("appointmentCount", appointmentCount);
	    return "Numberofappointmentsforagivendayresult";
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
	    model.addAttribute("patientId", patientId);
	    return "indexPatient";
	}

	@GetMapping("/markAppointment/{patientId}")
	public String markAppointment(
	    @PathVariable("patientId") long patientId,
	    Model model
	) {
	    model.addAttribute("patientId", patientId);

	    List<Doctor> doctors = doctorRepository.findAll();
	    model.addAttribute("doctors", doctors);

	    return "markAppointment";
	}
	
	@GetMapping("/seeAppointments/{patientId}")
	public String seeAppointments(
	    @PathVariable("patientId") long patientId,
	    Model model
	) {
	    model.addAttribute("patientId", patientId);
	    
	    List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

	    model.addAttribute("ListAppointments", appointments);

	    return "seeAppointments";
	}
	
	@GetMapping("/deleteAppointment/{id}/{patientId}")
	public String deletePatient(@PathVariable(value = "id") Long id, @PathVariable(value = "patientId") long patientId) {
		appointmentRepository.deleteById(id);
		return "redirect:/seeAppointments/" + patientId;
	}

	@PostMapping("/saveAppointment")
	public String Appointment(@ModelAttribute("new_appointment") Appointment newAppointment, @RequestParam("patientId") Long patientId) {
		
		Patient patient = getPatientById(patientId);
	    newAppointment.setPatient(patient);

	    appointmentRepository.save(newAppointment);
	    return "redirect:/indexPatient/" + patientId;
	}
	
	@GetMapping("/statsPatient/{patientId}")
	public String statsPatient(
			Model model,
			@PathVariable("patientId") long patientId
	) {
	    return "statsPatient";
	}
	
	
	@GetMapping("/numberAppointmentsForEachDoctor")
	public String numberAppointmentsForEachDoctor(
			Model model
	) { 
		List<Object[]> doctorAndAppointmentsCount = appointmentRepository.getNumberOfAppointmentsForEachDoctor();

	    List<Doctor> doctors = new ArrayList<>();
	    List<Long> appointmentCounts = new ArrayList<>();

	    for (Object[] doctorAppointmentCount : doctorAndAppointmentsCount) {
	        Doctor doctor = (Doctor) doctorAppointmentCount[0];
	        Long appointmentCount = (Long) doctorAppointmentCount[1];

	        doctors.add(doctor);
	        appointmentCounts.add(appointmentCount);
	    }

	    model.addAttribute("doctors", doctors);
	    model.addAttribute("appointmentCounts", appointmentCounts);
	    return "numberAppointmentsForEachDoctor";
	}
	
	@GetMapping("/specializationWithMoreTreatments")
	public String specializationWithMoreTreatments(
			Model model
	) { 
		List<Object[]> specializationsAndTreatments = doctorRepository.findSpecializationWithMoreTreatments();
	    List<String> specializationNames = new ArrayList<>();
	    List<Long> specializationCounts = new ArrayList<>();

	    for (Object[] specialization : specializationsAndTreatments) {
	        specializationNames.add((String) specialization[0]);
	        specializationCounts.add((Long) specialization[1]);
	    }

	    model.addAttribute("specializationNames", specializationNames);
	    model.addAttribute("specializationCounts", specializationCounts);

	    return "specializationWithMoreTreatments";
	}
	
	
	public Patient getPatientById(Long id) {
	    Optional<Patient> optionalPatient = patientRepository.findById(id);
	    return optionalPatient.orElse(null);
	}
	
	public Appointment getAppointmentById(Long id) {
	    Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
	    return optionalAppointment.orElse(null);
	}

	/*
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
	*/
}
