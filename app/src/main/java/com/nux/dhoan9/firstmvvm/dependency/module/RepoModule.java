package com.nux.dhoan9.firstmvvm.dependency.module;

import com.nux.dhoan9.firstmvvm.data.repo.CartRepo;
import com.nux.dhoan9.firstmvvm.data.repo.CartRepoImpl;
import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.data.repo.DishRepoImpl;
import com.nux.dhoan9.firstmvvm.data.repo.OrderRepo;
import com.nux.dhoan9.firstmvvm.data.repo.OrderRepoImpl;
import com.nux.dhoan9.firstmvvm.data.repo.TodoRepo;
import com.nux.dhoan9.firstmvvm.data.repo.TodoRepoImpl;
import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.data.repo.UserRepoImpl;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hoang on 28/03/2017.
 */
@Module
public class RepoModule {

    @Provides
    public UserRepo provideUserRepo(Retrofit retrofit) {
        return new UserRepoImpl(retrofit);
    }

    @Provides
    public TodoRepo provideTodoRepo() {
        return new TodoRepoImpl();
    }

    @Provides
    public DishRepo provideDishRepo(Retrofit retrofit) {
        return new DishRepoImpl(retrofit);
    }

    @Provides
    public CartRepo provideCartRepo(Retrofit retrofit) {
        return new CartRepoImpl(retrofit);
    }

    @Provides
    public OrderRepo provideOrderRepo(Retrofit retrofit) {
        return new OrderRepoImpl(retrofit);
    }
}
