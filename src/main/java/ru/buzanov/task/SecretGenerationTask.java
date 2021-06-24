package ru.buzanov.task;

import ru.buzanov.bot.Bot;
import ru.buzanov.bot.IBotController;
import ru.buzanov.holder.ISecretHolder;

public class SecretGenerationTask implements Runnable{
    final ISecretHolder secretHolder;
    final IBotController bot;

    public SecretGenerationTask(ISecretHolder secretHolder, IBotController bot) {
        this.secretHolder = secretHolder;
        this.bot = bot;
    }


    @Override
    public void run() {
        bot.sendToChanel(secretHolder.generateSecret());
        Bot.unlock();
    }
}
