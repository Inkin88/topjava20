package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealMapStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet("/meals")
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealMapStorage storage = new MealMapStorage();

    public MealServlet() {
        initStorage();
    }

    private void initStorage() {
        List<Meal> mealList = MealsUtil.getMealList();
        for (Meal ml : mealList) {
            storage.create(ml);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Meal> meals = storage.getMealMap().values();
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );
        List<MealTo> mealsTo = new ArrayList<>();
        for (Meal meal : meals) {
            mealsTo.add(new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesSumByDate.get(meal.getDate()) > 2000));
        }
        String action = req.getParameter("action");
        if (action == null) {
            req.setAttribute("mealList", mealsTo);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            return;
        }
        Meal meal;
        switch (action) {
            case "delete":
                int id = Integer.parseInt(req.getParameter("id"));
                storage.delete(id);
                resp.sendRedirect("meals");
                return;
            case "edit":
                id = Integer.parseInt(req.getParameter("id"));
                meal = storage.get(id);
                break;
            case "add":
                meal = new Meal();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
        req.setAttribute("meal", meal);
        req.getRequestDispatcher("/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime ldt = LocalDateTime.parse(req.getParameter("localDateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        int id = Integer.parseInt(req.getParameter("id"));
        Meal meal;
        if (id == 0) {
            meal = new Meal(ldt, description, calories);
            storage.getMealMap().put(meal.getId(), meal);
        } else {
            meal = storage.get(id);
            meal.setCalories(calories);
            meal.setDateTime(ldt);
            meal.setDescription(description);
        }
        resp.sendRedirect("meals");
    }

}
