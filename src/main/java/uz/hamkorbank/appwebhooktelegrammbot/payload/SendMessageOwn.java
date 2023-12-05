package uz.hamkorbank.appwebhooktelegrammbot.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageOwn {
    @JsonProperty(value = "chat_id")
    private String chatId;


    private String text;

    @JsonProperty(value = "reply_markup")
    private ReplyKeyboardMarkup replyKeyboardMarkup;




}
