package drg.mentalhealth.support.service;

import drg.mentalhealth.support.model.TherapistProfile;
import drg.mentalhealth.support.repository.TherapistProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TherapistProfileService {
    @Autowired
    private TherapistProfileRepository repository;

    public List<TherapistProfile> findAll() {
        return repository.findAll();
    }

    public Optional<TherapistProfile> findById(Long id) {
        return repository.findById(id);
    }

    public TherapistProfile save(TherapistProfile profile) {
        return repository.save(profile);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
