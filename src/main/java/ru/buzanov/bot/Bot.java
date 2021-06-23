package ru.buzanov.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.buzanov.rest.IRestClient;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {
    private final IRestClient service;

    private final IBotService botService;

    private final String botUsername;

    private final String botToken;

    public Bot(@Autowired IRestClient service,@Autowired IBotService botService,@Value("${bot.name}") String botUsername,@Value("${bot.token}") String botToken) {
        this.service = service;
        this.botService = botService;
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            SendMessage sendMessage = botService.processingMessage(update, service);
            if (!sendMessage.getText().isEmpty()){
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    log.warn("Ошибка при отправке сообщения", e);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
