package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealMapStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet("/meals")
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private Storage storage = new MealMapStorage();

    public void init() {
        List<Meal> mealList = MealsUtil.getMealList();
        for (Meal ml : mealList) {
            storage.create(ml);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = req.getParameter("action");
        if (action == null) {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(storage.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            req.setAttribute("mealList", mealsTo);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            return;
        }
        Meal meal;
        switch (action) {
            case "delete":
                int id = Integer.parseInt(req.getParameter("id"));
                log.debug("Delete meal :" + id);
                storage.delete(id);
                resp.sendRedirect("meals");
                return;
            case "edit":
                id = Integer.parseInt(req.getParameter("id"));
                log.debug("Edit meal :" + id);
                meal = storage.get(id);
                break;
            case "add":
                log.debug("Add meal");
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
        log.debug("Create or Update meal");
        req.setCharacterEncoding("UTF-8");
        LocalDateTime ldt = LocalDateTime.parse(req.getParameter("localDateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        int id = Integer.parseInt(req.getParameter("id"));
        log.debug("Parametres: id "+ id + "Date " + ldt + "calories " + calories + "description " + description);
        if (id == 0) {
            storage.create(new Meal(ldt, description, calories));
            log.debug("Meal added");
        } else {
            storage.update(new Meal(id, ldt, description, calories));
            log.debug("Meal updated");
        }
        resp.sendRedirect("meals");
    }

}
