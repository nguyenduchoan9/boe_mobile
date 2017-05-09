package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nux.dhoan9.firstmvvm.data.error.RegisterError;
import com.nux.dhoan9.firstmvvm.data.repo.UserRepo;
import com.nux.dhoan9.firstmvvm.data.request.user.UserRegisterParam;
import com.nux.dhoan9.firstmvvm.data.response.RegisterResponse;
import com.nux.dhoan9.firstmvvm.data.validator.RegisterValidator;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.ErrorUtils;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.view.custom.TextChange;

import java.util.List;
import java.util.Map;

import okhttp3.Headers;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hoang on 27/03/2017.
 */

public class RegisterViewModel extends BaseViewModel {
    UserRepo userRepo;
    PreferencesManager preferencesManager;
    RegisterValidator registerValidator;

    private String email = "";
    public ObservableField<String> emailError = new ObservableField<>();
    private String password = "";
    public ObservableField<String> passwordError = new ObservableField<>();
    private String passwordConfirm = "";
    public ObservableField<String> passwordConfirmError = new ObservableField<>();

    public RegisterViewModel(@NonNull PreferencesManager preferencesManager,
                             @NonNull Resources resources,
                             @NonNull ThreadScheduler threadScheduler,
                             @NonNull RegisterValidator registerValidator,
                             @NonNull UserRepo userRepo) {
        super(threadScheduler, resources);
        this.userRepo = userRepo;
        this.preferencesManager = preferencesManager;
        this.registerValidator = registerValidator;
    }


    public TextChange onEmailChange = value -> {
        email = value;
        validateEmail();
    };

    private void validateEmail() {
        emailError.set(registerValidator.validateEmail(email));
    }

    public TextChange onPasswordChange = value -> {
        password = value;
        validatePassword();
    };

    private void validatePassword() {
        passwordError.set(registerValidator.validatePassword(password));
    }

    public TextChange onPasswordConfirmChange = value -> {
        passwordConfirm = value;
        validatePasswordConfirm();
    };

    private void validatePasswordConfirm() {
        passwordConfirmError.set(registerValidator.validatePasswordConfirm(password, passwordConfirm));
    }

    public Observable<Response<User>> register() {
        emailError.set(registerValidator.validateEmail(email));
        passwordError.set(registerValidator.validatePassword(password));
        passwordConfirmError.set(registerValidator.validatePasswordConfirm(password, passwordConfirm));
        return registerValidator.validate() ? registerTask() : Observable.empty();
    }

    public Observable<Response<User>> registerTask() {
        return userRepo.register(prepareParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::isSuccess);
    }

    private void isSuccess(Response<User> userResponse) {
        if (userResponse.isSuccessful()) {
            if (null != userResponse.body()) {
                saveCredentialHeader(userResponse.headers());
                saveUser(userResponse.body());
            }
        }else {
            handleError(userResponse);
        }
    }

    private void handleError(Response response) {
        try {
            RegisterError error = ErrorUtils.parseError(response, RegisterError.class);
            if (null != error && null != error.getError()) {
                List<String> err;
                if (null != error.getError().getMessage().getEmail()) {
                    err = error.getError().getMessage().getEmail();
                    emailError.set(err.get(0));
                }
                if (null != error.getError().getMessage().getPassword()){
                    err = error.getError().getMessage().getPassword();
                    passwordError.set(err.get(0));
                }
                if (null != error.getError().getMessage().getPasswordConfirmation()){
                    err = error.getError().getMessage().getPasswordConfirmation();
                    passwordError.set(err.get(0));
                }
            }
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

    private UserRegisterParam prepareParams() {
        UserRegisterParam params = new UserRegisterParam("hoang", "nguyen",
                email, password, passwordConfirm);
        return params;
    }
}

