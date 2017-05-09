package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.model.Dish;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by hoang on 08/05/2017.
 */

public class DishRepoImpl implements DishRepo {
    List<Dish> dishes;

    public DishRepoImpl() {
        dishes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dishes.add(new Dish(i, "Dish - " + i, 9999,
                    i % 2 == 0 ? "chocolate_ball.jpg" : "chocolate_cake.jpg"));
        }
    }

    @Override
    public List<Dish> getDishes() {
        return dishes;
    }

    private int indexOf(Dish dish) {
        return dishes.indexOf(dish);
    }

    @Override
    public Dish getDishDetail(int id) {
        for (Dish dish : dishes) {
            if (dish.getId() == id) {
                return dish;
            }
        }
        return null;
    }
}
