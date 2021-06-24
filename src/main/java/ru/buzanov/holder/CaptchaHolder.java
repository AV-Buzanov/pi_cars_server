package ru.buzanov.holder;

import lombok.SneakyThrows;
import nl.captcha.Captcha;
import nl.captcha.gimpy.*;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.noise.NoiseProducer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import javax.imageio.ImageIO;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Component
@Singleton
public class CaptchaHolder implements ICaptchaHolder{
    private Captcha captcha;

    @SneakyThrows
    public InputFile generateCaptcha() {
        captcha = new Captcha.Builder(125, 50)
                .addText()
                .addNoise(new CurvedLineNoiseProducer())
                .build();
        InputFile inputFile = new InputFile();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(captcha.getImage(), "png", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return inputFile.setMedia(is, "captcha.png");
    }

    public boolean checkCaptcha(String stringSecret) {
        return captcha.getAnswer().equals(stringSecret);
    }

}
