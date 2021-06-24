package ru.buzanov.holder;

import ru.buzanov.CarType;

public interface IUsersHolder {

    UserDetails getUserDetails(String firstName, Long id);

    UserDetails makeActive(Long id);

    void releaseCar(CarType carType);

}
