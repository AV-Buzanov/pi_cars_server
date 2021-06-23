package ru.buzanov.rest;

import ru.buzanov.CarType;

public interface IRestClient {
    String send(CarType carType, String command, int delay);
}
