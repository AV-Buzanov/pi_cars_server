package ru.buzanov.task;

import ru.buzanov.CarType;
import ru.buzanov.holder.IUsersHolder;

public class DeactivationTask implements Runnable{
    private final CarType carType;
    private final IUsersHolder usersHolder;

    public DeactivationTask(CarType carType, IUsersHolder usersHolder) {
        this.carType = carType;
        this.usersHolder = usersHolder;
    }

    @Override
    public void run() {
        usersHolder.releaseCar(carType);
    }
}
