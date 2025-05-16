package drg.mentalhealth.support.repository;

import drg.mentalhealth.support.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByTherapistIdAndAppointmentTimeBetween(Long therapistId, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByTherapistId(Long therapistId);
    long countByAppointmentTimeAfter(LocalDateTime dateTime);
}

