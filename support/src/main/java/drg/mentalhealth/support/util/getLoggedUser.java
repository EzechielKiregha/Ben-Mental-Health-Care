package drg.mentalhealth.support.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import drg.mentalhealth.support.model.User;
import drg.mentalhealth.support.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class getLoggedUser {

  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private UserService userService;
  
  public User CurrentLoggedInUser(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
      if (header == null || !header.startsWith("Bearer ")) {
          return null;
      }
      String token = header.substring(7);
      String email = jwtUtil.extractEmail(token);

      User user = userService.getUserByEmail(email).get();

      return user;
  }
}
