package com.nux.dhoan9.firstmvvm.dependency.component;

import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.dependency.module.DishModule;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.view.activity.DishesByCategoryActivity;
import com.nux.dhoan9.firstmvvm.view.fragment.CutleryFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.DrinkingFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.MenuFragment;

import dagger.Subcomponent;
/**
 * Created by hoang on 08/05/2017.
 */
@ActivityScope
@Subcomponent(modules = {ActivityModule.class, DishModule.class})
public interface DishComponent {
    void inject(MenuFragment menuFragment);
    void inject(DishesByCategoryActivity dishesByCategoryActivity);
    void inject(CutleryFragment cutleryFragment);
    void inject(DrinkingFragment drinkingFragment);
}
