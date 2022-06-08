package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

       // List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        //mealsTo.forEach(System.out::println);



        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMeal> sortedList = new ArrayList<UserMeal>(meals);
        Collections.sort(sortedList, Comparator.comparing(UserMeal::getDateTime));
        List<UserMealWithExcess> resultList = new ArrayList<>();// TODO return filtered list with excess. Implement by cycles
        List<UserMeal> tmpList = new ArrayList<>();
        for (UserMeal user : sortedList) {
            if (tmpList.size()==0) {
                tmpList.add(user);
            } else if (user.getDateTime().toLocalDate().isEqual(tmpList.get(0).getDateTime().toLocalDate())) {
                tmpList.add(user);
            } else {
                    convertAndAdd(tmpList, resultList, caloriesPerDay, startTime, endTime);
                    tmpList.clear();
                    tmpList.add(user);
                }
            }
        convertAndAdd(tmpList, resultList, caloriesPerDay, startTime, endTime);



        return resultList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDay = meals.stream().collect(Collectors.groupingBy(x -> x.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(um -> TimeUtil.isBetweenHalfOpen(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> new UserMealWithExcess(um.getDateTime(), um.getDescription(), um.getCalories(), caloriesSumByDay.get(um.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());



        //System.out.println(result);// TODO Implement by streams
        //return null;
    }

    private static boolean isCaloriesExceed(List<UserMeal> meals, int caloriesLimit) {
        int cal = 0;
        for (UserMeal meal : meals) {
            cal+=meal.getCalories();
        }
        return cal>caloriesLimit;
    }
    private static void convertAndAdd(List<UserMeal> source, List<UserMealWithExcess> target, int calLimit, LocalTime startTime, LocalTime endTime) {
        boolean isEx = isCaloriesExceed(source, calLimit);
        for (UserMeal meal : source) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                target.add(new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        isEx));
            }
        }
    }
}
