package ru.buzanov.bot;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.buzanov.CarType;

public interface IBotService {
    String processingMessage(Update update, CarType carType);
}
