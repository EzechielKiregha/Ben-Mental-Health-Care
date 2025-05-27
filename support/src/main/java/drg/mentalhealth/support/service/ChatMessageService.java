package drg.mentalhealth.support.service;

import drg.mentalhealth.support.model.ChatMessage;
import drg.mentalhealth.support.model.ChatSession;
import drg.mentalhealth.support.repository.ChatMessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage sendMessage(ChatSession session, String sender, String text) {
        ChatMessage message = new ChatMessage();
        message.setSession(session);
        if(sender.equals("PATIENT")) {
            message.setSender(ChatMessage.Sender.PATIENT);
        } else if (sender.equals("THERAPIST")) {
            message.setSender(ChatMessage.Sender.THERAPIST);
        }
        message.setMessageText(text);
        message.setSentAt(LocalDateTime.now());
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessageHistory(ChatSession session) {
        return chatMessageRepository.findAllBySession(session);
    }
}
