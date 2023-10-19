package uz.hamkorbank.appwebhooktelegrammbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.hamkorbank.appwebhooktelegrammbot.domain.Servers;
import uz.hamkorbank.appwebhooktelegrammbot.payload.ResultTelegramm;
import uz.hamkorbank.appwebhooktelegrammbot.payload.ServerDto;
import uz.hamkorbank.appwebhooktelegrammbot.repository.ServerRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepository serverRepository;
    private final RestTemplate restTemplate;

    public ResponseEntity<?> create(ServerDto dto) {
        Servers server = Servers.builder()
                .name(dto.getName())
                .status(true)
                .build();
        Servers saveServers = serverRepository.save(server);
        return ResponseEntity.ok(saveServers);
    }

    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(serverRepository.findAll());
    }

    public ResponseEntity<?> deleteById(Integer id) throws ClassNotFoundException {
        try {
            serverRepository.deleteById(id);
            return ResponseEntity.ok("Deleted success");
        } catch (Exception e) {
            throw new ClassNotFoundException("Data not found");

        }
    }

    public void scheduledServer() {
        List<Servers> serversList = null;
        serversList = serverRepository.findAll();
        for (Servers servers : serversList) {
            if (servers.getLast_date()!=null) {
                if (servers.getLast_date().before(Date.from(Instant.now()))) {
                    servers.setTryCount(servers.getTryCount() + 1);
                    serverRepository.save(servers);
                }
            }
            if (servers.getTryCount()>0){
                if (servers.getTryCount() > 6) {

                    SendMessage sendMessage = new SendMessage(servers.getChatId(),
                            "Sizga tegishli " + servers.getName()
                                    + " yakunlandi Agar udalyonka ishltayotgan bolsanggiz qayta o'zlashtirib oling ");
                    ResultTelegramm resultTelegramm = restTemplate.postForObject(
                            RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                            sendMessage, ResultTelegramm.class);
                    servers.setStatus(true);
                    servers.setChatId(null);
                    servers.setUsername(null);
                    servers.setTryCount(0);
                    servers.setLast_date(null);
                    serverRepository.save(servers);
                }
            }
        }
    }
}
