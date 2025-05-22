package drg.mentalhealth.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import drg.mentalhealth.support.model.ChatSession;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
  List<ChatSession> findAllByUserId(Long id);
}
