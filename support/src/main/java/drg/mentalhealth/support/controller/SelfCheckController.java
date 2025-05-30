package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.dto.SelfCheckRequest;
import drg.mentalhealth.support.dto.SelfCheckResponse;
import drg.mentalhealth.support.model.SelfCheckResult;
import drg.mentalhealth.support.service.SelfCheckResultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/self-check")
public class SelfCheckController {

    @Autowired
    private SelfCheckResultService selfCheckResultService;

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public SelfCheckResponse saveResult(@RequestBody SelfCheckRequest req, @RequestParam  Long userId) {
        return selfCheckResultService.saveResult(req, userId);
    }

    @GetMapping("/user/{userId}")
    public List<SelfCheckResult> getResultsByUserId(@PathVariable Long userId) {
        return selfCheckResultService.getResultsByUserId(userId);
    }
}
