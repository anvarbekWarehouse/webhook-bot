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
import uz.hamkorbank.appwebhooktelegrammbot.feign.TelegramFeign;
import uz.hamkorbank.appwebhooktelegrammbot.payload.*;
import uz.hamkorbank.appwebhooktelegrammbot.repository.ServerRepository;
import uz.hamkorbank.appwebhooktelegrammbot.repository.UserRepository;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebhookService {
    private final RestTemplate restTemplate;

    private final TelegramFeign telegramFeign;
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;





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

//        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
//        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
//        var yesButton = new InlineKeyboardButton();
//
//        yesButton.setText("Yes");
//        yesButton.setCallbackData(YES_BUTTON);
//
//        var noButton = new InlineKeyboardButton();
//
//        noButton.setText("No");
//        noButton.setCallbackData(NO_BUTTON);
//
//        rowInLine.add(yesButton);
//        rowInLine.add(noButton);
//
//        rowsInLine.add(rowInLine);

//        markupInLine.setKeyboard(rowsInLine);
        SendMessageOwn sendMessage = new SendMessageOwn(update.getMessage().getChatId().toString(),
                "Xush kelibsiz "+update.getMessage().getChat().getFirstName(), keyboardMarkup);

        ResultTelegramm resultTelegramm = restTemplate.postForObject(
                RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                sendMessage, ResultTelegramm.class);
        System.out.println(resultTelegramm);
    }

    public void whenStatus(Update update) {
        String systemPath = System.getProperty("user.dir");
        String path = systemPath + "/my_json.json";
        Gson gson = new Gson();
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            FidoRdp[] fidoRdps = gson.fromJson(reader, FidoRdp[].class);

            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            int i = 1;
            if (fidoRdps.length>0) {
                for (FidoRdp fidoRdp : fidoRdps) {
                    var button = new InlineKeyboardButton();
                    if (fidoRdp.isStatus()) {
                        button.setText("✅✅✅" + fidoRdp.getName());
                        button.setCallbackData(fidoRdp.getId().toString());
                    } else {
                        button.setText("‼‼‼⚠⚠⚠" + fidoRdp.getName());
                        button.setCallbackData(fidoRdp.getId().toString());
                        button.setUrl(fidoRdp.getUsername());
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
            System.out.println(resultTelegramm);
            } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
        public void whenTake(Update update) {
            System.out.println(update);
            String systemPath = System.getProperty("user.dir");
            String path = systemPath + "/my_json.json";
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();
            File file = new File(path);
            String bookingServer = null;
            String message = null;
            System.out.println(update.getCallbackQuery().getFrom().getUserName());
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                List<FidoRdp> fidoRdpList = new ArrayList<>();
                FidoRdp[] fidoRdps = gson.fromJson(reader, FidoRdp[].class);
                for (FidoRdp fidoRdp : fidoRdps) {

                    if (fidoRdp.getId().toString().equals(update.getCallbackQuery().getData())){
                        if (fidoRdp.isStatus()) {
                            bookingServer = fidoRdp.getName();
                            message = "Band qilindi ";
                            fidoRdp.setStatus(false);
                            String chatId = update.getCallbackQuery().getFrom().getId().toString();
                            fidoRdp.setChatId(chatId);
                            fidoRdp.setLast_date(Date.from(Instant.now()));
                            fidoRdp.setUsername("https://t.me/" + update.getCallbackQuery().getFrom().getUserName());
                        }else {
                            if (fidoRdp.getChatId().equals(update.getCallbackQuery().getFrom().getId().toString())){
                                bookingServer = fidoRdp.getName();
                                message = "Boshatildi ";
                                fidoRdp.setStatus(true);
                                fidoRdp.setLast_date(null);
                                fidoRdp.setChatId(null);
                                fidoRdp.setUsername(null);
                            }else {
                                bookingServer = null;
                                message = "Siz bu rdp ni band qilmagansiz ";
                            }
                        }
                    }
                    fidoRdpList.add(fidoRdp);
                }
                FileWriter fileWriter = new FileWriter(file);
                gson.toJson(fidoRdpList, fileWriter);
                fileWriter.close();
                ///////////////////////////////////
                InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
                List<InlineKeyboardButton> rowInLine = new ArrayList<>();
                BufferedReader readerAfterUpdate = new BufferedReader(new FileReader(file));
                FidoRdp[] updateRdps = gson.fromJson(readerAfterUpdate, FidoRdp[].class);
                int i = 1;

                for (FidoRdp updateRdp : updateRdps) {
                    var button = new InlineKeyboardButton();
                    if (updateRdp.isStatus()) {
                        button.setText("✅✅✅" + updateRdp.getName());
                        button.setCallbackData(updateRdp.getId().toString());
                    } else {
                        button.setText("‼‼‼⚠⚠⚠" + updateRdp.getName());
                        button.setCallbackData(updateRdp.getId().toString());
                        button.setUrl(updateRdp.getUsername());
                    }

                    rowInLine.add(button);
                    if (i % 2 == 0) {
                        rowsInLine.add(rowInLine);
                        rowInLine = new ArrayList<>();
                    }
                    i++;
                }
                markupInLine.setKeyboard(rowsInLine);
                SendMessageInlineKeyboard sendMessage = new SendMessageInlineKeyboard(update.getCallbackQuery().getFrom().getId().toString(),
                        message+bookingServer, markupInLine);
                ResultTelegramm resultTelegramm = restTemplate.postForObject(
                        RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                        sendMessage, ResultTelegramm.class);
                System.out.println(resultTelegramm);

            }catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }

    public void whenDefaultMessage(Update update) {
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(),
                "Mavjud bo'lmagan buyruq berildi.");

        ResultTelegramm resultTelegramm = restTemplate.postForObject(
                RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                sendMessage, ResultTelegramm.class);
    }

    public void whenFinished(Update update) {
        String systemPath = System.getProperty("user.dir");
        String path = systemPath + "/my_json.json";
        Gson gson = new Gson();
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            FidoRdp[] fidoRdps = gson.fromJson(reader, FidoRdp[].class);

            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            int i = 1;
            if (fidoRdps.length>0) {
                for (FidoRdp fidoRdp : fidoRdps) {
                    var button = new InlineKeyboardButton();
                    if (fidoRdp.isStatus()) {
                        button.setText("✅✅✅" + fidoRdp.getName());
                        button.setCallbackData(fidoRdp.getId().toString());
                    } else {
                        button.setText("‼‼‼⚠⚠⚠" + fidoRdp.getName());
                        button.setCallbackData(fidoRdp.getId().toString());
//                        button.setUrl(fidoRdp.getUsername());
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void whenRegister(Update update) {
        String systemPath = System.getProperty("user.dir");
        String path = systemPath + "/users.json";
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        File file = new File(path);
        Long chatId = update.getMessage().getChat().getId();
        if (update.getMessage().getText().equals("OK")){
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            }catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else if(update.getMessage().getText().equals("NO")){

        }else {
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row1 = new KeyboardRow();
            row1.add("OK✅");
            KeyboardRow row2 = new KeyboardRow();
            row2.add("NO⚠");
            row2.add(chatId.intValue(),"");
            keyboard.add(row1);
            keyboard.add(row2);
            keyboardMarkup.setKeyboard(keyboard);

            String id  = "5565614634";
            String text = update.getMessage().getChat().getFirstName() +
                    "\n  https://t.me/"+update.getMessage().getChat().getUserName();
            SendMessageOwn sendMessage = new SendMessageOwn(id, text, keyboardMarkup);
            ResultTelegramm resultTelegramm = restTemplate.postForObject(
                    RestConstants.TELEGRAM_BASE_URL + RestConstants.BOT_TOKEN + "/sendMessage",
                    sendMessage, ResultTelegramm.class);
            System.out.println(resultTelegramm);
        }

    }
}
