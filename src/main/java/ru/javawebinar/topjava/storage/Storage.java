package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

public interface Storage {
    void create(Meal meal);

    Meal get(Integer id);

    void delete(Integer id);

    void update(Meal meal);

}