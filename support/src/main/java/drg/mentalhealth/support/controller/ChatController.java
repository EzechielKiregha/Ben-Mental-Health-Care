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
        return chatSessionService.startSession(userId, therapistId);
    }

    @PostMapping("/{sessionId}/message")
    public ChatMessage sendMessage(@PathVariable Long sessionId, @RequestParam ChatMessage.Sender sender, @RequestParam String text) {
        ChatSession session = new ChatSession(); // Replace with actual session retrieval logic
        session.setId(sessionId);
        return chatMessageService.sendMessage(session, sender, text);
    }

    @GetMapping("/{sessionId}")
    public List<ChatMessage> getMessageHistory(@PathVariable Long sessionId) {
        ChatSession session = new ChatSession(); // Replace with actual session retrieval logic
        session.setId(sessionId);
        return chatMessageService.getMessageHistory(session);
    }
}
