package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.model.EmergencyContact;
import drg.mentalhealth.support.repository.EmergencyContactRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/emergency")
public class EmergencyController {

    private final EmergencyContactRepository emergencyContactRepository;

    public EmergencyController(EmergencyContactRepository emergencyContactRepository) {
        this.emergencyContactRepository = emergencyContactRepository;
    }

    @GetMapping
    public List<EmergencyContact> listAllContacts() {
        return emergencyContactRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<EmergencyContact> addContact(@RequestBody EmergencyContact emergencyContact) {
        return ResponseEntity.ok(emergencyContactRepository.save(emergencyContact));
    }
}
