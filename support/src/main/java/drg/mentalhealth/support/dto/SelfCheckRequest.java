package drg.mentalhealth.support.dto;

import java.time.Instant;
import java.util.List;

public record SelfCheckRequest( 
  Long usedId,
  List<Long> answers,
  Instant takenAt
) {
}