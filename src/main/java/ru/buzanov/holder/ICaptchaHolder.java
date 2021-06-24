package ru.buzanov.holder;

import org.telegram.telegrambots.meta.api.objects.InputFile;

public interface ICaptchaHolder {
    InputFile generateCaptcha();

    boolean checkCaptcha(String stringSecret);

}
