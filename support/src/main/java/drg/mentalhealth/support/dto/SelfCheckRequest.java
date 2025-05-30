package drg.mentalhealth.support.dto;

import java.time.Instant;
import java.util.List;

public record SelfCheckRequest( 
  Long usedId,
  Integer score,
  List<Integer> answers,
  Instant takenAt
) {
}