package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.model.User;
import drg.mentalhealth.support.service.AuthService;
import drg.mentalhealth.support.service.OTPService;
import drg.mentalhealth.support.service.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private OTPService otpService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User body) {
        String token = userService.registerNewUser(body);
        if (token != null) {
            return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
        } else {
            return ResponseEntity.status(400).body("{\"error\": \"User registration failed\"}");
        }
    }

    @PostMapping("/verify-otp/{email}/{otp}")
    public ResponseEntity<?> verifyOtP(@PathVariable String email, @PathVariable String genOTP) {
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent() && genOTP.equalsIgnoreCase("genOTP")) {
            User userData = user.get();
            if (otpService.validateOTP(userData.getEmail(), Integer.parseInt(genOTP))) {
                userData.setEnabled(true);
                userData.setOtpString(0); // Clear OTP after successful verification
                userService.updateUser(userData);
                otpService.clearOTP(userData.getEmail());
                return ResponseEntity.ok("{\"message\": \"OTP verified successfully\"}");
            } else {
                return ResponseEntity.status(400).body("{\"error\": \"Invalid OTP\"}");
            }
        } else {
            return ResponseEntity.status(404).body("{\"error\": \"User not found\"}");
        }

        
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestParam String email, @RequestParam String password) {
        try {
            String token = authService.authenticate(email, password);
            Optional<User> user = userService.getUserByEmail(email);
            if (user.isPresent() ){
                User userData = user.get();
                Integer otp = otpService.generateOTP(userData.getEmail());
                userData.setOtpString(otp);
                if(otpService.sendOTP(userData.getEmail(), "Verification Code | Ben Mental Health Care", "Your Verification Code is: " + otp)){
                    
                    userService.updateUser(userData);
                    return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
                }
            } else
                return ResponseEntity.status(400).body("{\"error\": \"User not found or OTP generation failed\"}");

            return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("{\"WHYYYYYYYY\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        boolean success = userService.resetPassword(email, newPassword);
        if (success) {
            return ResponseEntity.ok().body("{\"message\": \"Password reset successful\"}");
        } else {
            return ResponseEntity.status(404).body("{\"error\": \"User not found\"}");
        }
    }
}
