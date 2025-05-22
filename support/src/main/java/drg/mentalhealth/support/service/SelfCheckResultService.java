package drg.mentalhealth.support.service;

import drg.mentalhealth.support.dto.SelfCheckRequest;
import drg.mentalhealth.support.dto.SelfCheckResponse;
import drg.mentalhealth.support.model.SelfCheckResult;
import drg.mentalhealth.support.model.User;
import drg.mentalhealth.support.repository.MentalHealthResourceRepository;
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

    @Autowired
    private MentalHealthResourceRepository resourceRepository;

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
        for (int i = 0; i < answers.size(); i++) {
          if (answers.get(i) >= 2) {
            // fetch all resources tagged for question i
            var list = resourceRepository.findByQuestionIndex(i);
            list.forEach(r -> recs.add(r.getId()));
          }
        }
        return new ArrayList<>(recs);
    }
    

    public List<SelfCheckResult> getResultsByUserId(Long userId) {
        return selfCheckResultRepository.findByUserId(userId);
    }


    
}
