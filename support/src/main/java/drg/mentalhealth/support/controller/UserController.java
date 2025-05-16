package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.model.User;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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
        Role role = roleRepository.findByName(role);
        if (role == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role not found");
        }
        
        return ResponseEntity.ok(userService.getUserByRole(body.getRole()));
    }
    
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> whoAmI(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = header.substring(7);
        String email = jwtUtil.extractEmail(token);
        Map<String, Object> info = Map.of(
            "email", email,
            "valid", jwtUtil.validateToken(token, userService.getUserByEmail(email).get())
        );
        return ResponseEntity.ok(info);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) { 
        userService.deleteUser(id);
    }
}
