package ru.buzanov.holder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.buzanov.CarType;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
    private String firstName;
    private CarType carType;
    private Integer prior;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public Integer getPrior() {
        return prior;
    }

    public void setPrior(Integer prior) {
        this.prior = prior;
    }
}