package drg.mentalhealth.support.service;

import drg.mentalhealth.support.model.Appointment;
import drg.mentalhealth.support.model.Status;
import drg.mentalhealth.support.model.User;
import drg.mentalhealth.support.repository.AppointmentRepository;
import drg.mentalhealth.support.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    // Fetch appointments by user ID
    public List<Appointment> getAppointmentsByUserId(Long userId) {
        return appointmentRepository.findByUserId(userId);
    }

    // Fetch appointments by therapist ID
    public List<Appointment> getAppointmentsByTherapistId(Long therapistId) {
        return appointmentRepository.findByTherapistId(therapistId);
    }

    // Book a new appointment
    @Transactional
    public Appointment bookAppointment(Long userId, Long therapistId, LocalDateTime appointmentTime) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User therapist = userRepository.findById(therapistId).orElseThrow(() -> new RuntimeException("Therapist not found"));

        // Check if therapist is available at the given time
        boolean isAvailable = appointmentRepository.findByTherapistId(therapistId).stream()
                .noneMatch(a -> a.getAppointmentTime().equals(appointmentTime));

        if (!isAvailable) {
            throw new RuntimeException("Therapist is not available at the selected time");
        }

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setTherapist(therapist);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setStatus(Status.SCHEDULED);

        return appointmentRepository.save(appointment);
    }

    // Update appointment status
    public Appointment updateAppointmentStatus(Long appointmentId, Status status) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    // Delete an appointment
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
