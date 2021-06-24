package ru.buzanov.bot;

public interface IBotController {
    String WRONG_SECRET = "Неверный код";
    String BUSY = "Занято, попробуй позже";
    String ATTACH_USER = "Ты взял управление над %s машиной на %d секунд";
    String ATTACH_CHANEL = "%s взял управление над %s машиной";
    void sendToChanel(String puzzle);
}
