package ru.buzanov.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.buzanov.CarType;
import ru.buzanov.rest.IRestClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class BotService implements IBotService {
    private static final Pattern WORDS_PATTERN = Pattern.compile("[a-zA-Z]+");
    private static final Pattern DIGITS_PATTERN = Pattern.compile("\\d+");

    private final IRestClient service;

    public BotService(@Autowired IRestClient service) {
        this.service = service;
    }

    public String processingMessage(Update update, CarType carType) {
        Message message = update.getMessage();
        String text = message.getText();
        log.info("receive from bot: {}", text);
        String[] s = text.split(" ");
        int counter = 0;
        for (String cmd : s) {
            try {
                if (cmd.length() == 0) continue;
                if (counter > 5000) throw new IllegalArgumentException();
                Matcher wordsMatcher = WORDS_PATTERN.matcher(cmd);
                Matcher digitsMatcher = DIGITS_PATTERN.matcher(cmd);
                String wds = wordsMatcher.find() ? cmd.substring(wordsMatcher.start(), wordsMatcher.end()) : "";
                String dgt = digitsMatcher.find() ? cmd.substring(digitsMatcher.start(), digitsMatcher.end()) : "";
                if (wds.isBlank()) throw new IllegalArgumentException();
                int timer;
                if (dgt.isBlank())
                    timer = 100;
                else
                    timer = Integer.parseInt(dgt);
                if (timer > 2000) throw new NumberFormatException();
                service.send(carType, wds, timer);
                counter += timer + 100;
                Thread.sleep(100);
            } catch (Exception e) {
                break;
            }
        }
        return "ок";
    }

}
