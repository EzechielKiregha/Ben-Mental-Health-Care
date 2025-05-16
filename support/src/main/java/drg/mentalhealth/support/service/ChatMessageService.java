package drg.mentalhealth.support.service;

import drg.mentalhealth.support.model.ChatMessage;
import drg.mentalhealth.support.model.ChatSession;
import drg.mentalhealth.support.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage sendMessage(ChatSession session, ChatMessage.Sender sender, String text) {
        ChatMessage message = new ChatMessage();
        message.setSession(session);
        message.setSender(sender);
        message.setMessageText(text);
        message.setSentAt(LocalDateTime.now());
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessageHistory(ChatSession session) {
        return chatMessageRepository.findAllBySession(session);
    }
}
