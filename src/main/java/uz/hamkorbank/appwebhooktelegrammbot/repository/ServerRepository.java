package uz.hamkorbank.appwebhooktelegrammbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hamkorbank.appwebhooktelegrammbot.domain.Servers;

public interface ServerRepository extends JpaRepository<Servers, Integer> {
}
