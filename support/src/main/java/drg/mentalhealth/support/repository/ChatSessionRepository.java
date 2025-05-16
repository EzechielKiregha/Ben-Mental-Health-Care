package drg.mentalhealth.support.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import drg.mentalhealth.support.model.ChatSession;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
}
