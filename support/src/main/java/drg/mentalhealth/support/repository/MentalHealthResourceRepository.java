package drg.mentalhealth.support.repository;

import drg.mentalhealth.support.model.MentalHealthResource;
import drg.mentalhealth.support.model.ResourceType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MentalHealthResourceRepository extends JpaRepository<MentalHealthResource, Long> {
    @Query("SELECT COUNT(r) FROM User u JOIN u.savedResources r WHERE u.id = :userId")
    long countBySavedByUserId(@Param("userId") Long userId);
    long countByResourceType(ResourceType type);
    List<MentalHealthResource> findByQuestionIndex(int questionIndex);

}
