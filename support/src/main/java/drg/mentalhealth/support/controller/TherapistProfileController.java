package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.model.TherapistProfile;
import drg.mentalhealth.support.service.TherapistProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/therapists-profiles")
public class TherapistProfileController {
    @Autowired
    private TherapistProfileService service;

    @GetMapping
    public List<TherapistProfile> listAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TherapistProfile> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TherapistProfile create(@RequestBody TherapistProfile profile) {
        return service.save(profile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TherapistProfile> update(@PathVariable Long id, @RequestBody TherapistProfile profile) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        profile.setId(id);
        return ResponseEntity.ok(service.save(profile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
