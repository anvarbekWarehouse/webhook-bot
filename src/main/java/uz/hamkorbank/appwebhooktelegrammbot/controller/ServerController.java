package uz.hamkorbank.appwebhooktelegrammbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import uz.hamkorbank.appwebhooktelegrammbot.payload.ServerDto;
import uz.hamkorbank.appwebhooktelegrammbot.service.ServerService;

@RestController
@RequestMapping("/api/server")
@RequiredArgsConstructor
public class ServerController {
    private final ServerService serverService;


    @PostMapping
    public ResponseEntity<?> create(@RequestBody ServerDto dto){
        return serverService.create(dto);
    }
    @GetMapping
    public ResponseEntity<?> findAll(){
        return serverService.findAll();
    }
    @DeleteMapping
    public ResponseEntity<?> deleteById(@RequestParam(name = "id") Integer id) throws ClassNotFoundException {
        return serverService.deleteById(id);
    }

   @Scheduled(fixedRate = 3600000)
    public void scheduledServer(){
        serverService.scheduledServer();
   }

}
