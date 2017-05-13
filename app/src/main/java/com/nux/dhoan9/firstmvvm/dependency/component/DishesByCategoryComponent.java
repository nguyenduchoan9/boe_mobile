package com.nux.dhoan9.firstmvvm.dependency.component;

import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.dependency.module.DishByCategoryModule;
import com.nux.dhoan9.firstmvvm.dependency.module.DishModule;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.view.activity.DishesByCategoryActivity;

import dagger.Subcomponent;
/**
 * Created by hoang on 13/05/2017.
 */
@ActivityScope
@Subcomponent(modules = {DishByCategoryModule.class})
public interface DishesByCategoryComponent {
    void inject(DishesByCategoryActivity dishesByCategoryActivity);
}
