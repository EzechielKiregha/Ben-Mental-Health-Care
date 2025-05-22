package drg.mentalhealth.support.controller;

import drg.mentalhealth.support.model.ChatMessage;
import drg.mentalhealth.support.model.ChatSession;
import drg.mentalhealth.support.service.ChatMessageService;
import drg.mentalhealth.support.service.ChatSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatSessionService chatSessionService;
    
    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping("/start")
    public ChatSession startChat(@RequestParam Long userId, @RequestParam(required = false) Long therapistId) {
        ChatSession session = chatSessionService.resumeChatSession(userId, therapistId);
        if (session == null) {
            session = chatSessionService.startSession(userId, therapistId);
        }
        return session;
    }

    @PostMapping("/{sessionId}/message")
    public ChatMessage sendMessage(@PathVariable Long sessionId, @RequestParam ChatMessage.Sender sender, @RequestParam String text) {
        ChatSession session = chatSessionService.getChatSession(sessionId); // Replace with actual session retrieval logic
        return chatMessageService.sendMessage(session, sender, text);
    }

    @GetMapping("/{sessionId}")
    public List<ChatMessage> getMessageHistory(@PathVariable Long sessionId) {
        ChatSession session = chatSessionService.getChatSession(sessionId); // Replace with actual session retrieval logic
        if (session == null) {
            throw new RuntimeException("Session not found");
        }
        return chatMessageService.getMessageHistory(session);
    }
}
