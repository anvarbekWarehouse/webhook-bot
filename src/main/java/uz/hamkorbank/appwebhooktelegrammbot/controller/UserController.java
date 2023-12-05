package uz.hamkorbank.appwebhooktelegrammbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.hamkorbank.appwebhooktelegrammbot.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return userService.findAll();
    }
    @GetMapping("/enabled")
    public ResponseEntity<?> enabled(@RequestParam(name = "chat_id") Long chatId){
        return userService.enabled(chatId);
    }
    @DeleteMapping
    public ResponseEntity<?> delete(){
        return userService.delete();
    }


}
