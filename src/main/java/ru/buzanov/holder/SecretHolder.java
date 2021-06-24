package ru.buzanov.holder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@Singleton
public class SecretHolder implements ISecretHolder {
    private static final Random random = new Random();
    private final int min;
    private final int max;

    private long secret;

    public SecretHolder(
            @Value("${secret.min}") int min,
            @Value("${secret.max}") int max) {
        this.min = min;
        this.max = max;
    }

    public String generateSecret() {
        secret = getRandomNumber(min, max);
        int first = (int) (secret / 2);
        int last = (int) (secret - first);
        int randomNumber = IntStream.range(2, 200)
                .parallel()
                .unordered()
                .filter(number -> first % number == 0)
                .findAny()
                .orElse(1);
        int i = last / 3;
        return String.format("%d * %d + %d + %d =", first / randomNumber, randomNumber, last - i, i);
    }

    @Override
    public boolean checkSecret(String stringSecret) {
        try {
            long i = Long.parseLong(stringSecret.trim());
            if (secret == i) return true;
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    private int getRandomNumber(int min, int max) {
        return random.ints(min, max)
                .findFirst()
                .orElse(max);
    }
}
