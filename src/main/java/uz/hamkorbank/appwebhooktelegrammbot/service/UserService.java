package uz.hamkorbank.appwebhooktelegrammbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.hamkorbank.appwebhooktelegrammbot.domain.Users;
import uz.hamkorbank.appwebhooktelegrammbot.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<?> enabled(Long chatId) {
        Optional<Users> optionalUsers = userRepository.findByChatId(chatId);
        if (!optionalUsers.isPresent())
            return ResponseEntity.ok("Data not found");
        Users users = optionalUsers.get();
        if (users.isStatus()){
            users.setStatus(false);
        }else {
            users.setStatus(true);
        }
        Users saveUser = userRepository.save(users);
        return ResponseEntity.ok(saveUser);
    }

    public ResponseEntity<?> delete() {
        userRepository.deleteByStatus(false);
        return ResponseEntity.ok("OK");
    }


}
