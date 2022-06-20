package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;


public class MealsServlet extends HttpServlet {
private MealRepository repository;
private static final Logger log = getLogger(MealsServlet.class);

@Override
public void init(ServletConfig config) throws ServletException {
    super.init(config);
    repository = new InMemoryMealRepository();
}
/*
    List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );
    Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
            .collect(
                    Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//
            );
    List<MealTo> meals2 = meals.stream()
            .map(meal -> new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesSumByDate.get(meal.getDate()) > 2000))
            .collect(Collectors.toList());*/
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DateTimeParseException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty()? null: Integer.valueOf(id),
                LocalDateTime.parse(Objects.requireNonNull(request.getParameter("dateTime"))),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        log.info(meal.isNew()? "Create {}": "Update {}", meal);
        repository.save(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action==null) {
            log.info("getAll");
            request.setAttribute("mealsList", MealsUtil.convertTolMealsTo(repository.getAll(), 2000));
            request.getRequestDispatcher("/mealsList.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int id = getId(request);
            log.info("Delete {}", id);
            repository.delete(id);
            response.sendRedirect("meals");
        } else {
            final Meal meal = action.equals("create")?
                    new Meal(LocalDateTime.now(), "", 1000) :
                    repository.get(getId(request));
                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("mealEdit.jsp").forward(request, response);
        }
       /* log.debug("redirect to meals");
        RequestDispatcher view = request.getRequestDispatcher("mealsList.jsp");
        request.setAttribute("meals", meals2);
        view.forward(request, response);*/
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}