package drg.mentalhealth.support.dto;

import java.util.List;

public record SelfCheckResponse(
    Long id,
    int score,
    List<Long> recommendedResourceIds
) {}