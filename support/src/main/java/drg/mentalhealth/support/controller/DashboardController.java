package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.dto.DashboardStatsDto;
import drg.mentalhealth.support.service.DashboardService;
import drg.mentalhealth.support.util.getLoggedUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private getLoggedUser loggedUser;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats(HttpServletRequest request) {
        var user = loggedUser.CurrentLoggedInUser(request);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        DashboardStatsDto stats = dashboardService.getStats(user.getId());
        return ResponseEntity.ok(stats);
    }
}
