package drg.mentalhealth.support.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelfCheckResultDto {
  private Long id;
  private int score;
  private List<Long> recommendedResourceIds;
  // getters/setters
}

