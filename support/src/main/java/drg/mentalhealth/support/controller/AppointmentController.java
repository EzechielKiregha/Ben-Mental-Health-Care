package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.model.*;
import drg.mentalhealth.support.repository.AppointmentRepository;
import drg.mentalhealth.support.repository.UserRepository;

import drg.mentalhealth.support.util.getLoggedUser;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private getLoggedUser loggedUser;

    @GetMapping
    public ResponseEntity<?> viewAppointments(HttpServletRequest request) {

        User user = loggedUser.CurrentLoggedInUser(request);
        Long userId = user.getId();

        if (userId == null) return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");

        boolean isPatient = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_PATIENT"));
        boolean isTherapist = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_THERAPIST"));

        List<Appointment> appointments;

        if (isPatient) {
            appointments = appointmentRepository.findByUserId(userId);
        } else if (isTherapist) {
            appointments = appointmentRepository.findByTherapistId(userId);
        } else {
            return ResponseEntity.status(403).body("{\"error\": \"Forbidden\"}");
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

        appointmentRepository.delete(appointment);
        return ResponseEntity.ok("{\"message\": \"Appointment deleted successfully\"}");
    }
}
