package com.nux.dhoan9.firstmvvm.data.repo;

import android.util.Log;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.response.CartDishAvailable;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.DishSugesstion;
import com.nux.dhoan9.firstmvvm.model.MenuCategories;
import com.nux.dhoan9.firstmvvm.model.SuggestionByCategory;
import com.nux.dhoan9.firstmvvm.services.DishServices;
import com.nux.dhoan9.firstmvvm.services.UserServices;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by hoang on 08/05/2017.
 */

public class DishRepoImpl implements DishRepo {
    private final String LOG_TAG = DishRepoImpl.class.getSimpleName();
    Retrofit retrofit;
    DishServices services;

    public DishRepoImpl(Retrofit retrofit) {
        this.retrofit = retrofit;
        services = retrofit.create(DishServices.class);
    }

    @Override
    public Observable<List<MenuCategories>> getMenu() {
        return Observable.create(subscriber -> {
            services.getMenuCutlery(1)
                    .compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<List<MenuCategories>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(LOG_TAG, e.getMessage());
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(List<MenuCategories> menuCategories) {
                            subscriber.onNext(menuCategories);
                        }
                    });
        });
    }

    @Override
    public Observable<List<MenuCategories>> getMenuDrinking() {
        return Observable.create(subscriber -> {
            services.getMenuDrinking(1)
                    .compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<List<MenuCategories>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(LOG_TAG, e.getMessage());
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(List<MenuCategories> menuCategories) {
                            subscriber.onNext(menuCategories);
                        }
                    });
        });
    }

    @Override
    public Observable<Dish> getDishDetail(int id) {
        return Observable.create(subscriber -> {
            services.getDishDetail(id)
                    .compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<Dish>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(Dish dish) {
                            subscriber.onNext(dish);
                        }
                    });
        });
    }

    @Override
    public Observable<List<Dish>> getDishesByCategory(int idCategory, String keySeach) {
        return Observable.create(sub -> {
            services.getDishByCategory(idCategory, keySeach)
                    .compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<List<Dish>>() {
                        @Override
                        public void onCompleted() {
                            sub.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            sub.onError(e);
                        }

                        @Override
                        public void onNext(List<Dish> dish) {
                            sub.onNext(dish);
                        }
                    });
        });
    }

    @Override
    public Observable<List<MenuCategories>> getDrinkingByKeySearch(String keySearch) {
        return Observable.create(subscriber -> {
            services.getDrinkingByKeySearch(keySearch)
                    .compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<List<MenuCategories>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(List<MenuCategories> menuCategories) {
                            subscriber.onNext(menuCategories);
                        }
                    });
        });
    }

    @Override
    public Observable<List<MenuCategories>> getCutleryByKeySearch(String keySearch) {
        return Observable.create(subscriber -> {
            services.getCutleryByKeySearch(keySearch)
                    .compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<List<MenuCategories>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(List<MenuCategories> menuCategories) {
                            subscriber.onNext(menuCategories);
                        }
                    });
        });
    }

    @Override
    public Observable<List<CartDishAvailable>> checkDishCartAvailable(String ids) {
        return Observable.create(sub -> {
            services.checkDishInCartAvailable(ids)
                    .compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<List<CartDishAvailable>>() {
                        @Override
                        public void onCompleted() {
                            sub.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            sub.onError(e);
                        }

                        @Override
                        public void onNext(List<CartDishAvailable> cartDishAvailables) {
                            sub.onNext(cartDishAvailables);
                        }
                    });
        });
    }

    @Override
    public Observable<SuggestionByCategory> getSuggestedDish() {
        return services.getSuggestedDish();
    }

}
