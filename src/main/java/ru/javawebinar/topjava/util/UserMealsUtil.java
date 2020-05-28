package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {

    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDay = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            LocalDate key = getLocalDate(userMeal);
            caloriesByDay.merge(key, userMeal.getCalories(), Integer::sum);
        }
        for (UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(getLocalTime(userMeal), startTime, endTime)) {
                result.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), caloriesByDay.get(getLocalDate(userMeal)) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = meals.stream().collect(Collectors.toMap(UserMealsUtil::getLocalDate, UserMeal::getCalories, Integer::sum));
        return meals.stream()
                .filter(m -> TimeUtil.isBetweenHalfOpen(getLocalTime(m), startTime, endTime))
                .map(m -> new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), map.get(getLocalDate(m)) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static LocalTime getLocalTime(UserMeal userMeal) {
        return userMeal.getDateTime().toLocalTime();
    }

    private static LocalDate getLocalDate(UserMeal userMeal) {
        return userMeal.getDateTime().toLocalDate();
    }
}
