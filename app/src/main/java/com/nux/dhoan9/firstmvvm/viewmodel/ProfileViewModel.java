package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.HeaderCredential;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.StringUtils;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by hoang on 01/05/2017.
 */

public class ProfileViewModel extends BaseViewModel {
    public String email = "email";
    public String password = "password";
    public String firstName = "firstname";
    public String lastName = "lastname";
    public String address = "address";
    public String phone = " phone";

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getAddress() {
        return address;
    }
    public String getPhone() {
        return phone;
    }



    private UserRepo userRepo;
    private PreferencesManager preferencesManager;

    public ProfileViewModel(@NonNull ThreadScheduler threadScheduler,
                            @NonNull Resources resources,
                            @NonNull PreferencesManager preferencesManager,
                            @NonNull UserRepo userRepo) {
        super(threadScheduler, resources);
        this.preferencesManager = preferencesManager;
        this.userRepo = userRepo;
    }

    public Observable<Response<User>> initializeData() {
        HeaderCredential h = preferencesManager.getCredentialHeader();
        return userRepo.getUserProfile()
                .compose(withScheduler())
                .doOnNext(response -> getUserData(response));
    }

    private void getUserData(Response<User> response) {
        if (response.isSuccessful()) {
            if (null != response.body()) {
                User user = response.body();
//                email = user.getEmail();
//                password = user.getPhone();
//                firstName = user.getFirstName();
//                lastName = user.getLastName();
//                address = StringUtils.isBlank(user.getAddress()) ? "My Address" : user.getAddress();
//                phone = StringUtils.isBlank(user.getPhone()) ? "MyPhone" : user.getPhone();
            }
        }
    }
}
