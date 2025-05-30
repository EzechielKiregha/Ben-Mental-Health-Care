package drg.mentalhealth.support.service;

import drg.mentalhealth.support.model.ChatSession;
import drg.mentalhealth.support.model.User;
import drg.mentalhealth.support.repository.ChatSessionRepository;
import drg.mentalhealth.support.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ChatSessionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;


    public ChatSession startSession(Long userId, Long therapistId) {
        ChatSession session = new ChatSession();
        User user = userRepository.findById(userId).get();
        session.setUser(user); // Assuming User has a constructor with ID
        if (therapistId != null) {
            User therapist = userRepository.findById(therapistId).orElse(null);
            session.setTherapist(therapist); // Assuming TherapistProfile has a constructor with ID
        }
        session.setStartedAt(LocalDateTime.now());
        return chatSessionRepository.save(session);
    }

    public ChatSession resumeChatSession(Long userId, Long therapistId) {

        List<ChatSession> allSessions = chatSessionRepository.findAll();

        if (allSessions.size() != 0){

            Optional<ChatSession> chatSess = allSessions.stream()
                .filter(
                chatSession -> chatSession.getTherapist().getId().equals(therapistId) && chatSession.getUser().getId().equals(userId))
                .findAny();

                if (chatSess.isPresent()) {
                    return chatSess.get();
                } else {
                    return null;
                }
        }

        return null;
    }

    public List<ChatSession> getAllSessionsByUserId(Long userId){

        List<ChatSession> chatSess = chatSessionRepository.findAll();

        List<ChatSession> chatSessions = chatSess.stream()
            .filter(chatS -> chatS.getUser().getId().equals(userId))
            .collect(java.util.stream.Collectors.toList());
        return chatSessions;
    }

    public ChatSession getChatSession(long id){
        Optional<ChatSession> s = chatSessionRepository.findById(id);
        if (s.isPresent()) {
            return s.get();
        }
        return null;
    }
}
