package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MealMapStorage implements Storage {
    private static final AtomicInteger counter = new AtomicInteger(1);

    private Map<Integer, Meal> mealMap = new HashMap<>();

    @Override
    public void create(Meal meal) {
        meal.setId(counter.getAndIncrement());
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

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }
}
