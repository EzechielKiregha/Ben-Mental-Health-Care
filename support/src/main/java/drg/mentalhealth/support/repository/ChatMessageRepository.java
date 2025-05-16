package drg.mentalhealth.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import drg.mentalhealth.support.model.ChatMessage;
import drg.mentalhealth.support.model.ChatSession;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  List<ChatMessage> findAllBySession(ChatSession session);
}
