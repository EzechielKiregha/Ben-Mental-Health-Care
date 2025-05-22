package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.model.Role;
import drg.mentalhealth.support.model.User;
import drg.mentalhealth.support.repository.RoleRepository;
import drg.mentalhealth.support.service.UserService;
import drg.mentalhealth.support.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User body) {
        return ResponseEntity.ok(body);
    }

    @PostMapping("/role")
    public ResponseEntity<?> getUserByRole(@RequestParam String role) {
        if (role == null || role.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role is required");
        }
        Optional<Role> r = roleRepository.findByName(role);
        if (!r.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role not found");
        }
        
        return ResponseEntity.ok(userService.getUserByRole(r.get()));
    }
    
    @PostMapping("/me")
    public ResponseEntity<?> whoAmI(@RequestParam Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) { 
        userService.deleteUser(id);
    }
}
