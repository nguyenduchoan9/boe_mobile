package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.MenuCategories;
import com.nux.dhoan9.firstmvvm.model.Todo;

import java.util.List;
import rx.Observable;

/**
 * Created by hoang on 08/05/2017.
 */

public interface DishRepo {
    Observable<List<MenuCategories>> getMenu();
    Observable<List<MenuCategories>> getMenuDrinking();

    Observable<Dish> getDishDetail(int id);

    Observable<List<Dish>> getDishesByCategory(int idCategory, String keySeach);
    Observable<List<MenuCategories>> getDrinkingByKeySearch(String keySearch);
    Observable<List<MenuCategories>> getCutleryByKeySearch(String keySearch);
}
