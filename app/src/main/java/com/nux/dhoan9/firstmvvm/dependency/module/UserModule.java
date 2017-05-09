package com.nux.dhoan9.firstmvvm.dependency.module;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.data.validator.LoginValidator;
import com.nux.dhoan9.firstmvvm.data.validator.RegisterValidator;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.viewmodel.LoginViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.ProfileViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.RegisterViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hoang on 28/03/2017.
 */

@Module
public class UserModule {

    @Provides
    public RegisterValidator provideRegisterValidator(@NonNull Resources resources) {
        return new RegisterValidator(resources);
    }

    @Provides
    public RegisterViewModel provideRegisterViewModel(
            @NonNull PreferencesManager preferencesManager,
            @NonNull Resources resources,
            @NonNull ThreadScheduler threadScheduler,
            @NonNull RegisterValidator registerValidator,
            @NonNull UserRepo userRepo) {
        return new RegisterViewModel(preferencesManager, resources,
                threadScheduler, registerValidator, userRepo);
    }

    @Provides
    public LoginValidator provideLoginValidator(@NonNull Resources resources) {
        return new LoginValidator(resources);
    }

    @Provides
    public LoginViewModel provideLoginViewModel(@NonNull UserRepo userRepo,
                                                @NonNull LoginValidator loginValidator,
                                                @NonNull PreferencesManager preferencesManager,
                                                @NonNull ThreadScheduler threadScheduler,
                                                @NonNull Resources resources) {
        return new LoginViewModel(userRepo, loginValidator,
                preferencesManager, threadScheduler, resources);
    }

    @Provides
    public ProfileViewModel provideProfileViewModel(@NonNull ThreadScheduler threadScheduler,
                                                    @NonNull Resources resources,
                                                    @NonNull PreferencesManager preferencesManager,
                                                    @NonNull UserRepo userRepo) {
        return new ProfileViewModel(threadScheduler, resources,
                preferencesManager, userRepo);
    }

}
