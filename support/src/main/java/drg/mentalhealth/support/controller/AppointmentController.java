package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.model.*;
import drg.mentalhealth.support.repository.AppointmentRepository;
import drg.mentalhealth.support.repository.UserRepository;

import drg.mentalhealth.support.service.AppointmentService;
import drg.mentalhealth.support.util.getLoggedUser;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private getLoggedUser loggedUser;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<?> viewAppointments(HttpServletRequest request, @RequestParam long userId) {
        
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("{\"error\": \"User not found\"}");
        }
        User user = optionalUser.get();
        boolean isPatient = user.getRoles().stream().anyMatch(role -> role.getName().equals("PATIENT"));
        boolean isTherapist = user.getRoles().stream().anyMatch(role -> role.getName().equals("THERAPIST"));

        List<Appointment> appointments;

        if (isPatient) {
            appointments = appointmentService.getAppointmentsByUserId(userId);
        } else if (isTherapist) {
            appointments = appointmentService.getAppointmentsByTherapistId(userId);
        } else {
            return ResponseEntity.ok().body("{\"error\": \"NO FOUND\"}");
        }

        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestParam Long therapistId, @RequestParam String appointmentTime, HttpServletRequest request) {
        User user = loggedUser.CurrentLoggedInUser(request);
        Long userId = user.getId();
        if (userId == null) return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");

        User therapist = userRepository.findById(therapistId).orElseThrow();
        LocalDateTime dateTime = LocalDateTime.parse(appointmentTime);

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setTherapist(therapist);
        appointment.setAppointmentTime(dateTime);
        appointment.setStatus(Status.SCHEDULED);

        appointmentRepository.save(appointment);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> updateAppointmentStatus(@RequestParam Long appointmentId, @RequestParam Status status, HttpServletRequest request) {
        User user = loggedUser.CurrentLoggedInUser(request);
        Long therapistId = user.getId();
        if (therapistId == null) return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        if (status.equals(Status.SCHEDULED)){
            appointment.setStatus(Status.SCHEDULED);
        } else if (status.equals(Status.CANCELLED)) {
            appointment.setStatus(Status.CANCELLED);
        } else if (status.equals(Status.COMPLETED)) {
            appointment.setStatus(Status.COMPLETED);
        } else {
            return ResponseEntity.status(400).body("{\"error\": \"Invalid status\"}");
        }
        
        appointmentRepository.save(appointment);

        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id, HttpServletRequest request) {
        User user = loggedUser.CurrentLoggedInUser(request);
        Long userId = user.getId();
        if (userId == null) return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");

        Appointment appointment = appointmentRepository.findById(id).orElseThrow();
        if (!appointment.getUser().getId().equals(userId)) {
            return ResponseEntity.status(403).body("{\"error\": \"Forbidden\"}");
        }

        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("{\"message\": \"Appointment deleted successfully\"}");
    }
}
