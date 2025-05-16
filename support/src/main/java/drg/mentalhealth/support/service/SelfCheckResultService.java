package drg.mentalhealth.support.service;

import drg.mentalhealth.support.dto.SelfCheckRequest;
import drg.mentalhealth.support.dto.SelfCheckResponse;
import drg.mentalhealth.support.model.SelfCheckResult;
import drg.mentalhealth.support.model.User;
import drg.mentalhealth.support.repository.SelfCheckResultRepository;
import drg.mentalhealth.support.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class SelfCheckResultService {
    @Autowired
    private SelfCheckResultRepository selfCheckResultRepository;

    @Autowired
    private UserRepository userRepo;

    public SelfCheckResponse saveResult(SelfCheckRequest req) {
        User user = userRepo.findById(req.usedId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        SelfCheckResult entity = new SelfCheckResult();
        entity.setUser(user);
        entity.setScore(req.answers().stream().mapToInt(Long::intValue).sum());
        try {
            entity.setAnswersJson(new ObjectMapper().writeValueAsString(req.answers()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
        entity.setTakenAt(req.takenAt());
        // 1) Save the result entity as you do now
        SelfCheckResult saved = selfCheckResultRepository.save(entity);

        // 2) Compute recommendations
        List<Integer> answers = saved.getAnswers();
        List<Long> recommendedResourceIds = computeRecommendations(answers);

        SelfCheckResponse out = new SelfCheckResponse(saved.getId(), saved.getScore(), recommendedResourceIds);

        return out;
    }

    private List<Long> computeRecommendations(List<Integer> answers) {
        Set<Long> recs = new LinkedHashSet<>();

        // Q1 (index 0): interest/pleasure → suggest resources 1, 3
        if (answers.get(0) >= 2) {              // “More than half the days” or “Nearly every day”
            recs.add(1L);
            recs.add(3L);
        }

        // Q2 (index 1): feeling down → suggest resource 2
        if (answers.get(1) >= 2) {
            recs.add(2L);
        }

        // Q3 (index 2): sleep trouble → suggest resource 4
        if (answers.get(2) >= 2) {
            recs.add(4L);
        }

        // Q4 (index 3): low energy → suggest resource 5
        if (answers.get(3) >= 2) {
            recs.add(5L);
        }

        // Q5 (index 4): appetite changes → suggest resource 6
        if (answers.get(4) >= 2) {
            recs.add(6L);
        }

        // Q6 (index 5): concentration → suggest resource 7
        if (answers.get(5) >= 2) {
            recs.add(7L);
        }

        // Q7 (index 6): anxiety → suggest resource 8
        if (answers.get(6) >= 2) {
            recs.add(8L);
        }

        return new ArrayList<>(recs);
    }

    public List<SelfCheckResult> getResultsByUserId(Long userId) {
        return selfCheckResultRepository.findByUserId(userId);
    }


    
}
