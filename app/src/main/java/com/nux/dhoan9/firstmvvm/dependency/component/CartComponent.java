package com.nux.dhoan9.firstmvvm.dependency.component;

import com.nux.dhoan9.firstmvvm.dependency.module.CartModule;
import com.nux.dhoan9.firstmvvm.dependency.scope.CartScope;
import com.nux.dhoan9.firstmvvm.view.activity.CartActivity;

import dagger.Subcomponent;
/**
 * Created by hoang on 12/05/2017.
 */
@CartScope
@Subcomponent(modules = {CartModule.class})
public interface CartComponent {
    void inject(CartActivity cartActivity);
}
