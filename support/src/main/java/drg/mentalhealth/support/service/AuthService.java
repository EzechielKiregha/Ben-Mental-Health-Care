package drg.mentalhealth.support.service;

import drg.mentalhealth.support.model.User;
import drg.mentalhealth.support.repository.UserRepository;
import drg.mentalhealth.support.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticate(String Email, String password) {
        User user = userRepository.findByEmail(Email).get();
        if( user != null && passwordEncoder.matches(password, user.getPassword())){
            return jwtUtil.generateToken(user);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public User getUserByEmail(String Email) {
        return userRepository.findByEmail(Email).get();
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        return userRepository.save(user);
    }

}
