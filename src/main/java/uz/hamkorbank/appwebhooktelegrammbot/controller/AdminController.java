package uz.hamkorbank.appwebhooktelegrammbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import uz.hamkorbank.appwebhooktelegrammbot.feign.TelegramFeign;
import uz.hamkorbank.appwebhooktelegrammbot.payload.ResultTelegramm;
import uz.hamkorbank.appwebhooktelegrammbot.payload.SendPhotoOwn;
import uz.hamkorbank.appwebhooktelegrammbot.service.RestConstants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final TelegramFeign telegramFeign;
     private Set<String> userChatId = new HashSet<>(Arrays.asList("5806745727"));

    @GetMapping("/sendMesage")
    public String sendMessageToUser(@RequestParam String text){
        for (String id : userChatId) {
            SendMessage sendMessage = new SendMessage(id, text);
            ResultTelegramm resultTelegramm = telegramFeign.sendMessageToUser("bot"
                    + RestConstants.BOT_TOKEN, sendMessage);

            System.out.println(resultTelegramm);
        }
        return "Xabar yuborildi!!";
    }

    @GetMapping("/sendImage")
    public void sendImage(@RequestParam String fileName,
                          @RequestParam String text){
        String fileId = null;
        for (String id : userChatId) {
            try{
                fileName = fileId == null ? RestConstants.MY_URL+"/api/attachment?fileName="+fileName : fileId;
                SendPhotoOwn sendPhoto = new SendPhotoOwn(
                        id,
                        text,
                        fileName
                );

                System.out.println(sendPhoto);
                ResultTelegramm resultTelegramm = telegramFeign.sendPhotoToUser("bot"
                        + RestConstants.BOT_TOKEN, sendPhoto);
                List<PhotoSize> photos = resultTelegramm.getResult().getPhoto();
                PhotoSize photoSize = photos.get(photos.size() - 1);
                fileId  = photoSize.getFileId();
                System.out.println(resultTelegramm);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
