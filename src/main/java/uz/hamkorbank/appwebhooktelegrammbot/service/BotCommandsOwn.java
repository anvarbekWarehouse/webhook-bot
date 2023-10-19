package uz.hamkorbank.appwebhooktelegrammbot.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BotCommandsOwn {

    private List<BotCommand> commands;
}
