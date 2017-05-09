package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.data.request.user.LoginUserParam;
import com.nux.dhoan9.firstmvvm.data.request.user.UserRegisterParam;
import com.nux.dhoan9.firstmvvm.data.response.LoginResponse;
import com.nux.dhoan9.firstmvvm.model.User;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by hoang on 27/03/2017.
 */

public interface UserRepo {
    Observable<Response<User>> register(UserRegisterParam params);
    Observable<Response<User>> getUserProfile();
    Observable<Response<User>> loginByEmail(LoginUserParam params);
}
