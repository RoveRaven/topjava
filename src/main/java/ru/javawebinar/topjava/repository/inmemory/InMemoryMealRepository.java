package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::defaultSave);
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (!repository.containsKey(userId)) {
            repository.put(userId, new ConcurrentHashMap<>());
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    public Meal defaultSave(Meal meal) {
        if (!repository.containsKey(1)) {
            repository.put(1, new ConcurrentHashMap<>());
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.get(1).put(meal.getId(), meal);
            return meal;
        }
       return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int mealId) {

        return repository.get(userId).get(mealId);
    }

    @Override
    public List getAll(int userId) {
        log.info("getAll");

        return getAllFiltered(userId, meal -> true);



    }

    public List<Meal> getBetweenHalfOpen (LocalDateTime start, LocalDateTime end, int userId){
        return getAllFiltered(userId, meal -> Util.isBetweenHalfOpen(meal.getDateTime(),  start, end));
    }

    private List<Meal> getAllFiltered (int userId, Predicate<Meal> filter){
        Map<Integer, Meal>  map = repository.get(userId);
        return CollectionUtils.isEmpty(map)? Collections.emptyList() :
                map.values()
                        .stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }
}

