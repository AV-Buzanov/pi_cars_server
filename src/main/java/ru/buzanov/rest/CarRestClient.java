package ru.buzanov.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.buzanov.CarType;

import java.net.URI;

@Component
@Slf4j
public class CarRestClient implements IRestClient {

    private final RestTemplate restTemplate;

    private final String carHost;

    public CarRestClient(@Autowired RestTemplate restTemplate, @Value("${car.host}") String carHost) {
        this.restTemplate = restTemplate;
        this.carHost = carHost;
    }

    private final String pathTemplate = "%s?command=%s&delay=%d";

    public String send(CarType carType, String command, int delay) {
        log.info("send: {}, with command: {}, delay: {}", carHost, command, delay);
        String path = carHost + String.format(pathTemplate, carType.name().toLowerCase(), command, delay);
        HttpHeaders headers = new HttpHeaders();
        headers.set("command", command);
        headers.set("delay", String.valueOf(delay));
        RequestEntity<String> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(path));
        try {
            return restTemplate.exchange(requestEntity, String.class).getBody();
        } catch (RestClientException e) {
            log.warn("Ошибка при отправке комманды", e);
        }
        return null;
    }
}
