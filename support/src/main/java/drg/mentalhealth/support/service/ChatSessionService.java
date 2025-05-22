package drg.mentalhealth.support.service;

import drg.mentalhealth.support.model.ChatSession;
import drg.mentalhealth.support.model.User;
import drg.mentalhealth.support.repository.ChatSessionRepository;
import drg.mentalhealth.support.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatSessionService {

    @Autowired
    private UserRepository userRepository;

    private final ChatSessionRepository chatSessionRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository) {
        this.chatSessionRepository = chatSessionRepository;
    }

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
        // user.getRoles().stream().map(Role::getName).toList()

        List<ChatSession> allSessions = chatSessionRepository.findAll();

        Stream<ChatSession> chatSession = allSessions.stream().map(t -> {
            User therapist = t.getTherapist();
            User patient = t.getUser();
            
            if (therapist.getId().equals(therapistId) && patient.getId().equals(userId)) {
                return t;
            }
            return null;
        });

        Optional<ChatSession> session = chatSession.findAny();
        if (session.isPresent()) {
            return session.get();
        } else {
            return null;
        }
    }

    public List<ChatSession> getAllSessionsByUserId(Long userId){
        return chatSessionRepository.findAllByUserId(userId);
    }

    public ChatSession getChatSession(long id){
        Optional<ChatSession> s = chatSessionRepository.findById(id);
        if (s.isPresent()) {
            return s.get();
        }
        return null;
    }
}
