package drg.mentalhealth.support.repository;

import drg.mentalhealth.support.model.SelfCheckResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SelfCheckResultRepository extends JpaRepository<SelfCheckResult, Long> {
    List<SelfCheckResult> findByUserId(Long userId);
    long countByUserId(Long userId);
}
