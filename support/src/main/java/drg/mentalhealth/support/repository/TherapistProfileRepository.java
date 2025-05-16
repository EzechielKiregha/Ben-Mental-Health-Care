package drg.mentalhealth.support.repository;

import drg.mentalhealth.support.model.TherapistProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TherapistProfileRepository extends JpaRepository<TherapistProfile, Long> {
    TherapistProfile findByUserId(Long userId);
}
