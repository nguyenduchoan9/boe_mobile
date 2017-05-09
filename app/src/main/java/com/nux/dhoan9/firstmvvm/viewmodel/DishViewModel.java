package com.nux.dhoan9.firstmvvm.viewmodel;

import com.nux.dhoan9.firstmvvm.model.Dish;
/**
 * Created by hoang on 08/05/2017.
 */

public class DishViewModel {
    public final int id;
    public final String name;
    public final float price;
    public final String image;

    public DishViewModel(Dish dish) {
        this.id = dish.getId();
        this.name = dish.getName();
        this.price = dish.getPrice();
        this.image = dish.getImage();
    }

    public Dish toModel(DishViewModel viewModel) {
        return new Dish(id, name, price, image);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DishViewModel) {
            DishViewModel other = (DishViewModel) obj;
            return other.id == this.id;
        }
        return super.equals(obj);
    }
}
