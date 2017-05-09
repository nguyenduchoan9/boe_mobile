package com.nux.dhoan9.firstmvvm.dependency.component;

import com.nux.dhoan9.firstmvvm.dependency.module.UserModule;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.view.activity.ProfileActivity;
import com.nux.dhoan9.firstmvvm.view.fragment.LoginFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.RegisterFragment;

import dagger.Subcomponent;

/**
 * Created by hoang on 28/03/2017.
 */
@ActivityScope
@Subcomponent(modules = {UserModule.class})
public interface UserComponent {
    void inject(LoginFragment loginFragment);
    void inject(RegisterFragment registerFragment);
    void inject(ProfileActivity profileActivity);
}
