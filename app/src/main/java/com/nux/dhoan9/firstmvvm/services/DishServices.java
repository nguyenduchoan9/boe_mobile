package com.nux.dhoan9.firstmvvm.services;

import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.MenuCategories;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hoang on 28/05/2017.
 */

public interface DishServices {

    @GET("dishes/{id}/cutlery")
    Observable<List<MenuCategories>> getMenuCutlery(@Path("id") long id);

    @GET("dishes/{id}/drinking")
    Observable<List<MenuCategories>> getMenuDrinking(@Path("id") long id);

    @GET("dishes/{id}")
    Observable<Dish> getDishDetail(@Path("id") int id);

    @GET("dishes/by_category")
    Observable<List<Dish>> getDishByCategory(@Query("id") int id);


}
