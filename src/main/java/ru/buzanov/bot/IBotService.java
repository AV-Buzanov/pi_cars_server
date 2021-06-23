package ru.buzanov.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.buzanov.rest.IRestClient;

public interface IBotService {
    SendMessage processingMessage(Update update, IRestClient service);
}
