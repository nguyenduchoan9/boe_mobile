package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.nux.dhoan9.firstmvvm.data.error.LoginError;
import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.data.request.user.LoginUserParam;
import com.nux.dhoan9.firstmvvm.data.response.LoginResponse;
import com.nux.dhoan9.firstmvvm.data.validator.LoginValidator;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.ErrorUtils;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.view.custom.TextChange;

import okhttp3.Headers;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by hoang on 27/03/2017.
 */

public class LoginViewModel extends BaseViewModel {
    private String email = "";
    private String password = "";
    public ObservableField<String> emailError = new ObservableField<>();
    public ObservableField<String> passwordError = new ObservableField<>();
    private BehaviorSubject<String> toast = BehaviorSubject.create();

    private UserRepo userRepo;
    private LoginValidator loginValidator;
    private PreferencesManager preferencesManager;

    public LoginViewModel(@NonNull UserRepo userRepo,
                          @NonNull LoginValidator loginValidator,
                          @NonNull PreferencesManager preferencesManager,
                          @NonNull ThreadScheduler threadScheduler,
                          @NonNull Resources resources) {
        super(threadScheduler, resources);
        this.userRepo = userRepo;
        this.loginValidator = loginValidator;
        this.preferencesManager = preferencesManager;
    }

    public Observable<String> toast() {
        return toast.asObservable();
    }

    public TextChange onEmailChange = value -> {
        email = value;
        validateEmail();
    };

    private void validateEmail() {
        emailError.set(loginValidator.validateEmail(email));
    }

    public TextChange onPasswordChange = value -> {
        password = value;
        validatePassword();
    };

    private void validatePassword() {
        passwordError.set(loginValidator.validatePassword(password));
    }

    public Observable<Response<User>> login() {
        validateEmail();
        validatePassword();
        return loginValidator.isValidate() ? loginTask() : Observable.empty();
    }

    public Observable<Response<User>> loginTask() {
        LoginUserParam params = new LoginUserParam(email, password);
        return userRepo.loginByEmail(params)
                .compose(withScheduler())
                .doOnNext(this::isSuccess);
    }

    @VisibleForTesting
    public void isSuccess(Response<User> userResponse) {
        if (userResponse.isSuccessful()) {
            if (null != userResponse.body()) {
                saveCredentialHeader(userResponse.headers());
                saveUser(userResponse.body());
            }
        } else {
            handleError(userResponse);
        }
    }

    private void handleError(Response<User> userResponse) {
        try {
            LoginError errors = ErrorUtils.parseError(userResponse, LoginError.class);
            toast.onNext(errors.getError().getCode().toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void saveUser(User user) {
        preferencesManager.logIn(user);
    }

    private void saveCredentialHeader(Headers headers) {
        preferencesManager.saveCredentialHeader(headers);
    }

}
