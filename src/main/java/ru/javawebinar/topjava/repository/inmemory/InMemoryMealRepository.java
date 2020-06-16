package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(m -> save(m, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        } else if (repository.getOrDefault(userId, new ConcurrentHashMap<>()).get(meal.getId()) == null) {
            return null;
        }
        repository.putIfAbsent(userId, new ConcurrentHashMap<>());
        repository.get(userId).put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).values().stream()
                .sorted(Comparator.comparing(Meal::getDate))
                .collect(Collectors.toList());
    }
    @Override
    public List<Meal> getAllFilterByDate(int userId, LocalDate startDate, LocalDate endDate) {
       return repository.getOrDefault(userId, new ConcurrentHashMap<>()).values().stream()
                .filter(m -> DateTimeUtil.isBetweenHalfOpen(m.getDateTime(), startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)))
                .sorted(Comparator.comparing(Meal::getTime).reversed())
                .collect(Collectors.toList());
    }
}

