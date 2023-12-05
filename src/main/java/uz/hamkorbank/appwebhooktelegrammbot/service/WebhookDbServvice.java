package uz.hamkorbank.appwebhooktelegrammbot.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.hamkorbank.appwebhooktelegrammbot.domain.Servers;
import uz.hamkorbank.appwebhooktelegrammbot.domain.Users;
import uz.hamkorbank.appwebhooktelegrammbot.payload.FidoRdp;
import uz.hamkorbank.appwebhooktelegrammbot.payload.ResultTelegramm;
import uz.hamkorbank.appwebhooktelegrammbot.payload.SendMessageInlineKeyboard;
import uz.hamkorbank.appwebhooktelegrammbot.payload.SendMessageOwn;
import uz.hamkorbank.appwebhooktelegrammbot.repository.ServerRepository;
import uz.hamkorbank.appwebhooktelegrammbot.repository.UserRepository;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebhookDbServvice {
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final RestTemplate restTemplate;

    public void whenDefaultMessage(Update update) {

        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), "Mavjud bo'lmagan buyruq berdinggiz!");

        ResultTelegramm resultTelegramm = restTemplate.postForObject(
                RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                sendMessage, ResultTelegramm.class);
    }
    public void whenStart(Update update){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("RESTART🔄");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("BOOKING🎯");
        KeyboardRow row3 = new KeyboardRow();
        row3.add("FINISHED🏳");
        KeyboardRow row4 = new KeyboardRow();
        row3.add("REGISTER📝");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboardMarkup.setKeyboard(keyboard);


        SendMessageOwn sendMessage = new SendMessageOwn(update.getMessage().getChatId().toString(),
                "Xush kelibsiz "+update.getMessage().getChat().getFirstName(), keyboardMarkup);

        ResultTelegramm resultTelegramm = restTemplate.postForObject(
                RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                sendMessage, ResultTelegramm.class);
        System.out.println(resultTelegramm);
    }
    public void whenStatus(Update update) {
        Long chatId = update.getMessage().getChatId();
        Optional<Users> optionalUsers = userRepository.findByChatId(chatId);
        if (!optionalUsers.isPresent()){
            SendMessage sendMessage = new SendMessage(chatId.toString(), "Siz tasdiqdan otmagansiz!!");
            ResultTelegramm resultTelegramm = restTemplate.postForObject(
                    RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                    sendMessage, ResultTelegramm.class);
        }else if (!optionalUsers.get().isStatus()){
            SendMessage sendMessage = new SendMessage(chatId.toString(), "Siz tasdiqdan otmagansiz!!");
            ResultTelegramm resultTelegramm = restTemplate.postForObject(
                    RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                    sendMessage, ResultTelegramm.class);
        } else {
            List<Servers> serversList = serverRepository.findAll();
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            int i = 1;
            if (!serversList.isEmpty()) {
                for (Servers server : serversList) {
                    var button = new InlineKeyboardButton();
                    if (server.isStatus()) {
                        button.setText("✅✅✅" + server.getName());
                        button.setCallbackData(server.getId().toString());
                    } else {
                        button.setText("‼‼‼⚠⚠⚠" + server.getName());
                        button.setCallbackData(server.getId().toString());
                        button.setUrl(server.getUsername());
                    }

                    rowInLine.add(button);
                    if (i % 2 == 0) {
                        rowsInLine.add(rowInLine);
                        rowInLine = new ArrayList<>();
                    }
                    i++;
                }
            }
            markupInLine.setKeyboard(rowsInLine);
            SendMessageInlineKeyboard sendMessage = new SendMessageInlineKeyboard(update.getMessage().getChatId().toString(),
                    "Mavjud bo'lgan vertual kompyuterlar\nBand bo'lmagan kompyuterni tanlang", markupInLine);

            ResultTelegramm resultTelegramm = restTemplate.postForObject(
                    RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                    sendMessage, ResultTelegramm.class);
        }
    }

    public void whenRegister(Update update) throws ClassNotFoundException {
        Long chatId = update.getMessage().getChat().getId();
        String firstName = update.getMessage().getChat().getFirstName();
        String userName = update.getMessage().getChat().getUserName();
        if (userRepository.existsByChatId(chatId)) {
            Optional<Users> optionalUsers = userRepository.findByChatId(chatId);
            Users users = optionalUsers.get();
            if (!users.isStatus()) {
                SendMessage sendMessage = new SendMessage(chatId.toString(),
                        "Sizda tasdiqlanmagan profil bor Anvarbek Turdaliyev murojat qiling!!");
                ResultTelegramm resultTelegramm = restTemplate.postForObject(
                        RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                        sendMessage, ResultTelegramm.class);
            }
        } else {
                Users regUser = Users.builder()
                        .chatId(chatId)
                        .fullName(firstName)
                        .username(userName)
                        .status(false)
                        .build();
                userRepository.save(regUser);
                String id = "5565614634";
                String text = update.getMessage().getChat().getFirstName() +
                        "\n  https://t.me/" + update.getMessage().getChat().getUserName();
                SendMessage sendMessage = new SendMessage(id, text);
                ResultTelegramm resultTelegramm = restTemplate.postForObject(
                        RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                        sendMessage, ResultTelegramm.class);
            }

    }

    public void whenTake(Update update) {

            List<Servers> serversList = null;
            serversList = serverRepository.findAll();
            String bookingServer = "";
            String message = null;
            String chatId = update.getCallbackQuery().getFrom().getId().toString();
            String username = update.getCallbackQuery().getFrom().getUserName();
            for (Servers servers : serversList) {
                if (servers.getId().toString().equals(update.getCallbackQuery().getData())) {
                    if (servers.isStatus()) {
                        bookingServer = servers.getName();
                        message = "Band qilindi";
                        servers.setStatus(false);
                        servers.setChatId(chatId);
                        servers.setUsername("https://t.me/" + username);
                        servers.setLast_date(Date.from(Instant.now()));
                    } else {
                        if (servers.getChatId().equals(chatId)) {
                            bookingServer = servers.getName();
                            message = "Yakunlandi";
                            servers.setStatus(true);
                            servers.setChatId(null);
                            servers.setUsername(null);
                            servers.setLast_date(null);
                        } else {
                            bookingServer = "";
                            message = "Siz bu rdp ni band qilmagansiz ";
                        }
                    }
                }
                serverRepository.save(servers);
            }

            serversList = serverRepository.findAll();
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            int i = 1;
            if (!serversList.isEmpty()) {
                for (Servers server : serversList) {
                    var button = new InlineKeyboardButton();
                    if (server.isStatus()) {
                        button.setText("✅✅✅" + server.getName());
                        button.setCallbackData(server.getId().toString());
                    } else {
                        button.setText("‼‼‼⚠⚠⚠" + server.getName());
                        button.setCallbackData(server.getId().toString());
                        button.setUrl(server.getUsername());
                    }

                    rowInLine.add(button);
                    if (i % 2 == 0) {
                        rowsInLine.add(rowInLine);
                        rowInLine = new ArrayList<>();
                    }
                    i++;
                }
            }
            markupInLine.setKeyboard(rowsInLine);
            SendMessageInlineKeyboard sendMessage = new SendMessageInlineKeyboard(update.getCallbackQuery().getFrom().getId().toString(),
                    message + bookingServer, markupInLine);
            ResultTelegramm resultTelegramm = restTemplate.postForObject(
                    RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                    sendMessage, ResultTelegramm.class);

    }

    public void whenFinished(Update update) {
        Long chatIdR = update.getMessage().getChatId();
        Optional<Users> optionalUsers = userRepository.findByChatId(chatIdR);
        if (!optionalUsers.isPresent()) {
            SendMessage sendMessage = new SendMessage(chatIdR.toString(), "Siz tasdiqdan otmagansiz!!");
            ResultTelegramm resultTelegramm = restTemplate.postForObject(
                    RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                    sendMessage, ResultTelegramm.class);
        } else if (!optionalUsers.get().isStatus()) {
            SendMessage sendMessage = new SendMessage(chatIdR.toString(), "Siz tasdiqdan otmagansiz!!");
            ResultTelegramm resultTelegramm = restTemplate.postForObject(
                    RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                    sendMessage, ResultTelegramm.class);
        } else {
            List<Servers> serversList = serverRepository.findAll();
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            int i = 1;
            if (!serversList.isEmpty()) {
                for (Servers server : serversList) {
                    var button = new InlineKeyboardButton();
                    if (server.isStatus()) {
                        button.setText("✅✅✅" + server.getName());
                        button.setCallbackData(server.getId().toString());
                    } else {
                        button.setText("⚠" + server.getUsername().substring(13));
                        button.setCallbackData(server.getId().toString());
                    }

                    rowInLine.add(button);
                    if (i % 2 == 0) {
                        rowsInLine.add(rowInLine);
                        rowInLine = new ArrayList<>();
                    }
                    i++;
                }
            }
            markupInLine.setKeyboard(rowsInLine);
            SendMessageInlineKeyboard sendMessage = new SendMessageInlineKeyboard(update.getMessage().getChatId().toString(),
                    "O'zinggiz band qilgan serverni tanlang!!!", markupInLine);

            ResultTelegramm resultTelegramm = restTemplate.postForObject(
                    RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                    sendMessage, ResultTelegramm.class);
            System.out.println(resultTelegramm);

    }
    }
}

