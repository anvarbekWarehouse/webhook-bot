package uz.hamkorbank.appwebhooktelegrammbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hamkorbank.appwebhooktelegrammbot.domain.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByChatId(Long chatId);

    void deleteByStatus(boolean status);
    boolean existsByChatId(Long chatId);
}
