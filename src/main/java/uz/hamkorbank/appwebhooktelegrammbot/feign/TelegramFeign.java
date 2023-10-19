package uz.hamkorbank.appwebhooktelegrammbot.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import uz.hamkorbank.appwebhooktelegrammbot.payload.ResultTelegramm;
import uz.hamkorbank.appwebhooktelegrammbot.payload.SendPhotoOwn;
import uz.hamkorbank.appwebhooktelegrammbot.service.RestConstants;

@FeignClient(url = RestConstants.TELEGRAM_BASE_URL_WITHOUT_BOT,
            name = "TelegramFeign")
public interface TelegramFeign {

    @PostMapping("{path}/sendMessage")
    ResultTelegramm sendMessageToUser(@PathVariable String path,
                                      @RequestBody SendMessage sendMessage);


    @PostMapping("{path}/sendPhoto")
    ResultTelegramm sendPhotoToUser(@PathVariable String path,
                                      @RequestBody SendPhotoOwn sendPhotoOwn);

}
