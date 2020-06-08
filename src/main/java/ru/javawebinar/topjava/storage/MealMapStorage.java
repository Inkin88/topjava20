package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.HashMap;
import java.util.Map;

public class MealMapStorage implements Storage {

    private Map<Integer, Meal> mealMap = new HashMap<>();

    @Override
    public void create(Meal meal) {
        mealMap.putIfAbsent(meal.getId(), meal);
    }

    @Override
    public Meal get(Integer id) {
        return mealMap.get(id);
    }

    @Override
    public void delete(Integer id) {
        mealMap.remove(id);
    }

    @Override
    public void update(Meal meal) {
        mealMap.put(meal.getId(), meal);
    }

    public Map<Integer, Meal> getMealMap() {
        return mealMap;
    }
}
