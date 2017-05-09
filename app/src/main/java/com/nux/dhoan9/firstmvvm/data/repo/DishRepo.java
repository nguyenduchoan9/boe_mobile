package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.Todo;

import java.util.List;
/**
 * Created by hoang on 08/05/2017.
 */

public interface DishRepo {
    List<Dish> getDishes();
    Dish getDishDetail(int id);
}
