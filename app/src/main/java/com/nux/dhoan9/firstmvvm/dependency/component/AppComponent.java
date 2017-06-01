package com.nux.dhoan9.firstmvvm.dependency.component;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.dependency.module.AppModule;
import com.nux.dhoan9.firstmvvm.dependency.module.OrderModule;
import com.nux.dhoan9.firstmvvm.dependency.module.DishByCategoryModule;
import com.nux.dhoan9.firstmvvm.dependency.module.RepoModule;
import com.nux.dhoan9.firstmvvm.dependency.module.UserModule;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.activity.ChefActivity;
import com.nux.dhoan9.firstmvvm.view.activity.PaypalActivity;
import com.nux.dhoan9.firstmvvm.view.activity.SlashActivity;
import com.nux.dhoan9.firstmvvm.view.fragment.EndpointDialogFragment;
import com.nux.dhoan9.firstmvvm.view.fragment.QRCodeFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hoang on 27/03/2017.
 */
@Singleton
@Component(modules = {AppModule.class, RepoModule.class})
public interface AppComponent {
    UserComponent plus(UserModule userModule);
    DishComponent plus(ActivityModule activityModule);
    DishesByCategoryComponent plus(DishByCategoryModule dishByCategoryModule);
    OrderComponent plus(OrderModule cartModule);

    void inject(ChefActivity chefActivity);
    void inject(CustomerActivity customerActivity);
    void inject(SlashActivity slashActivity);

    void inject(QRCodeFragment qrCodeFragment);
    void inject(EndpointDialogFragment endpointDialogFragment);

    void inject(Application application);
    void inject(PaypalActivity paypalActivity);
}
