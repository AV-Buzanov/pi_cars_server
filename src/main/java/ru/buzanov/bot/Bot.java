package ru.buzanov.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.buzanov.holder.ICaptchaHolder;
import ru.buzanov.holder.ISecretHolder;
import ru.buzanov.holder.IUsersHolder;
import ru.buzanov.holder.UserDetails;
import ru.buzanov.task.DeactivationTask;
import ru.buzanov.task.SecretGenerationTask;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot implements IBotController {
    private final IBotService botService;

    private final String botUsername;

    private final String botToken;

    private final String chanelId;

    private final Integer userTime;

    private final IUsersHolder usersHolder;

    private final ISecretHolder secretHolder;

    private final ICaptchaHolder captchaHolder;

    private static Boolean LOCK = Boolean.FALSE;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public Bot(@Autowired IUsersHolder usersHolder,
               @Autowired IBotService botService,
               @Autowired ISecretHolder secretHolder,
               @Value("${bot.name}") String botUsername,
               @Value("${bot.token}") String botToken,
               @Value("${bot.chanel}") String chanelId,
               @Value("${car.holdTime}") Integer userTime,
               @Autowired ICaptchaHolder captchaHolder) {
        this.botService = botService;
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.usersHolder = usersHolder;
        this.chanelId = chanelId;
        this.secretHolder = secretHolder;
        this.userTime = userTime;
        this.captchaHolder = captchaHolder;
    }

    @PostConstruct
    public void init() {
//        sendToChanel(secretHolder.generateSecret());
        sendCaptchaToChanel(captchaHolder.generateCaptcha());
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            User from = update.getMessage().getFrom();
            String userName = from.getUserName() == null || from.getUserName().isEmpty() ? from.getFirstName() : from.getUserName();
            UserDetails userDetails = usersHolder.getUserDetails(userName, from.getId());
            if (userDetails.isActive()) {
                String s = botService.processingMessage(update, userDetails.getCarType());
                sendToUser(s, update);
                return;
            }

//            if (!secretHolder.checkSecret(update.getMessage().getText())) {
//                sendToUser(WRONG_SECRET, update);
//                return;
//            }
            if (!captchaHolder.checkCaptcha(update.getMessage().getText())) {
                sendToUser(WRONG_SECRET, update);
                return;
            }
            UserDetails details = usersHolder.makeActive(from.getId());
            if (!details.isActive()) {
                sendToUser(BUSY, update);
                return;
            }

            String toUser = String.format(ATTACH_USER, details.getCarType().name(), userTime);
            String toChanel = String.format(ATTACH_CHANEL, userName, details.getCarType().name());
            sendToChanel(toChanel);
            sendToUser(toUser, update);
            scheduler.schedule(new DeactivationTask(details.getCarType(), usersHolder)
                    , userTime, TimeUnit.SECONDS);
            if (!LOCK) {
                LOCK = true;
                scheduler.schedule(new SecretGenerationTask(secretHolder, captchaHolder, this)
                        , userTime + 1, TimeUnit.SECONDS);
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

    public void sendToUser(String message, Update update) {
        try {
            execute(SendMessage.builder().text(message).chatId(String.valueOf(update.getMessage().getChatId())).build());
        } catch (TelegramApiException e) {
            log.warn("Ошибка при отправке сообщения пользователю", e);
        }
    }

    @Override
    public void sendCaptchaToChanel(InputFile inputFile) {
        try {
            execute(SendPhoto.builder().photo(inputFile).chatId("@" + chanelId).build());
        } catch (TelegramApiException e) {
            log.warn("Ошибка при отправке капчи в канал", e);
        }
    }

    @Override
    public void sendToChanel(String puzzle) {
        try {
            execute(SendMessage.builder().text(puzzle).chatId("@" + chanelId).build());
        } catch (TelegramApiException e) {
            log.warn("Ошибка при отправке сообщения в канал", e);
        }
    }

    public static void unlock() {
        if (LOCK) {
            log.info("unlocked");
            LOCK = false;
        }
    }
}
