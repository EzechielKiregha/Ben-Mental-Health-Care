package drg.mentalhealth.support.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import drg.mentalhealth.support.model.EmergencyContact;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
}
