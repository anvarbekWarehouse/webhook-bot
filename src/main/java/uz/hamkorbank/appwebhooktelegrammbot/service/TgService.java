package uz.hamkorbank.appwebhooktelegrammbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class TgService {

  private final WebhookService webhookService;
  private final WebhookDbServvice webhookDbServvice;





    public void updateTelegram(Update update) throws ClassNotFoundException {
        if (update.hasCallbackQuery()){
            System.out.println("salom  "+update);
            webhookDbServvice.whenTake(update);
        }
        if (update.hasMessage()){
            String text = update.getMessage().getText();
            System.out.println(text);
            switch (text){
                case "/start":
                    webhookDbServvice.whenStart(update);
                    break;
                case "/status":
                    webhookDbServvice.whenStatus(update);
                    break;
                case "RESTART🔄":
                    webhookDbServvice.whenStatus(update);
                    break;
                case "BOOKING🎯":
                    webhookDbServvice.whenStatus(update);
                    break;
                case "FINISHED🏳":
                    webhookDbServvice.whenFinished(update);
                    break;
                case "REGISTER📝":
                    webhookDbServvice.whenRegister(update);
                    break;
                case "OK":
                    webhookService.whenRegister(update);
                    break;
                default:
                    webhookService.whenDefaultMessage(update);
            }
        }
    }
}
