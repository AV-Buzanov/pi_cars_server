package ru.buzanov.task;

import ru.buzanov.bot.Bot;
import ru.buzanov.bot.IBotController;
import ru.buzanov.holder.ICaptchaHolder;
import ru.buzanov.holder.ISecretHolder;

public class SecretGenerationTask implements Runnable{
    private final ISecretHolder secretHolder;
    private final ICaptchaHolder captchaHolder;

    private final IBotController bot;

    public SecretGenerationTask(ISecretHolder secretHolder, ICaptchaHolder captchaHolder, IBotController bot) {
        this.secretHolder = secretHolder;
        this.captchaHolder = captchaHolder;
        this.bot = bot;
    }


    @Override
    public void run() {
//        bot.sendToChanel(secretHolder.generateSecret());
        bot.sendCaptchaToChanel(captchaHolder.generateCaptcha());
        Bot.unlock();
    }
}
