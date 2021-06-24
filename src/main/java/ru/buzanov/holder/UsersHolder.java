package ru.buzanov.holder;

import org.springframework.stereotype.Component;
import ru.buzanov.CarType;

import javax.inject.Singleton;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Component
@Singleton
public class UsersHolder implements IUsersHolder {
    private final EnumMap<CarType, Long> cars = new EnumMap<>(CarType.class);
    private final Map<Long, UserDetails> users = new HashMap<>();

    public UsersHolder() {
        for (CarType value : CarType.values()) {
            cars.put(value, null);
        }
    }

    @Override
    public UserDetails getUserDetails(String user, Long id) {
        UserDetails details = UserDetails.builder()
                .active(false)
                .carType(CarType.BLUE)
                .prior(1)
                .firstName(user)
                .build();
        if (users.containsKey(id))
            return users.get(id);
        users.put(id, details);
        return details;
    }

    @Override
    public UserDetails makeActive(Long id) {
        UserDetails details = users.get(id);
        CarType carType = attachUserToFreeCar(id);
        if (carType == null) {
            return details;
        }
        details.setActive(true);
        details.setCarType(carType);
        return users.put(id, details);
    }

    @Override
    public void releaseCar(CarType carType) {
        Long s = cars.get(carType);
        UserDetails details = users.get(s);
        details.setActive(false);
        users.put(s, details);
        cars.put(carType, null);
    }


    private CarType attachUserToFreeCar(Long user) {
        CarType carType = null;
        for (Map.Entry<CarType, Long> carEntry : cars.entrySet()) {
            if (carEntry.getValue() == null) {
                carType = carEntry.getKey();
                break;
            }
        }
        if (carType == null) {
            return null;
        }
        cars.put(carType, user);
        return carType;
    }
}
