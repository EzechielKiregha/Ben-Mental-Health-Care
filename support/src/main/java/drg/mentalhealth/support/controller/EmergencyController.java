package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.model.EmergencyContact;
import drg.mentalhealth.support.model.User;
import drg.mentalhealth.support.repository.EmergencyContactRepository;
import drg.mentalhealth.support.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/emergency")
public class EmergencyController {

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<EmergencyContact> listAllContacts() {
        return emergencyContactRepository.findAll();
    }

    @PostMapping("/new-contact-line")
    public ResponseEntity<EmergencyContact> addContact(@RequestBody EmergencyContact emergencyContact, @RequestParam Long userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            emergencyContact.setUser(user.get());
        }else {
            throw new RuntimeException("[No User ID provided]");
        }
        return ResponseEntity.ok(emergencyContactRepository.save(emergencyContact));
    }
}
