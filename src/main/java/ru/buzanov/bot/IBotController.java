package ru.buzanov.bot;

import org.telegram.telegrambots.meta.api.objects.InputFile;

public interface IBotController {
    String WRONG_SECRET = "Неверный код";
    String BUSY = "Занято, попробуй позже";
    String ATTACH_USER = "Ты взял управление над %s машиной на %d секунд";
    String ATTACH_CHANEL = "%s взял управление над %s машиной";
    void sendToChanel(String puzzle);
    void sendCaptchaToChanel(InputFile inputFile);

}
