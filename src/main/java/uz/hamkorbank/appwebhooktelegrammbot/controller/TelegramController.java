package uz.hamkorbank.appwebhooktelegrammbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import uz.hamkorbank.appwebhooktelegrammbot.payload.FidoRdp;
import uz.hamkorbank.appwebhooktelegrammbot.service.TgService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramController {

    private final TgService tgService;


    @PostMapping
    public void getUpdates(@RequestBody Update update) throws ClassNotFoundException {
        tgService.updateTelegram(update);
        }

    @GetMapping("/menu_button")
    public ResponseEntity<?> getBotCommands(){
        List<FidoRdp> fidoRdpList = new ArrayList<>();
        fidoRdpList.add(new FidoRdp(1,null, "27 Fido1", true,
                null, null, Date.from(Instant.now())));
        fidoRdpList.add(new FidoRdp(2,null, "27 Fido2", true,
                null, null, Date.from(Instant.now())));
        fidoRdpList.add(new FidoRdp(3, null, "27 Fido3", true,
                null, null, Date.from(Instant.now())));
        fidoRdpList.add(new FidoRdp(4, null, "27 Fido4", true,
                null, null, Date.from(Instant.now())));
//        fidoRdpList.add(new FidoRdp(1,"27 Fido1", false, null,null, ));
//        fidoRdpList.add(new FidoRdp(2,"27 Fido2", false, null,null));
//        fidoRdpList.add(new FidoRdp(3,"27 Fido3", false, null,null));
//        fidoRdpList.add(new FidoRdp(4,"27 Fido4", false, null,null));

        return ResponseEntity.ok(fidoRdpList);
    }
}
