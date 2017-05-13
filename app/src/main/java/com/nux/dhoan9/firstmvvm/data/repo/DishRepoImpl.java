package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.MenuCategories;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by hoang on 08/05/2017.
 */

public class DishRepoImpl implements DishRepo {
    List<MenuCategories> menu;
    String[] titles = {"Suggest", "Discount", "Menu"};
    List<Dish> dishes;

    @Override
    public List<MenuCategories> getMenuCategories() {
        return menu;
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

    private int nextId() {
        return dishes.size();
    }

    public DishRepoImpl() {
        menu = new ArrayList<>();
        dishes = new ArrayList<>();
        for (int title = 0; title < titles.length; title++) {
            List<Dish> dishByCategory = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Dish dish = new Dish(nextId(), "Dish - " + i, 9999,
                        i % 2 == 0 ? R.drawable.chocolate_ball : R.drawable.chocolate_cake);
                dishes.add(dish);
                dishByCategory.add(dish);
            }
            menu.add(new MenuCategories(titles[title], dishByCategory));
        }

    }

    public List<Dish> getDishes(){
        return dishes;
    }
}
