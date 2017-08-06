package com.nux.dhoan9.firstmvvm.data.repo;

import android.util.Log;

import com.nux.dhoan9.firstmvvm.data.request.user.LoginUserParam;
import com.nux.dhoan9.firstmvvm.data.request.user.UserRegisterParam;
import com.nux.dhoan9.firstmvvm.data.response.Balance;
import com.nux.dhoan9.firstmvvm.data.response.LoginResponse;
import com.nux.dhoan9.firstmvvm.data.response.NotificationResponse;
import com.nux.dhoan9.firstmvvm.data.response.SessionDeleteResponse;
import com.nux.dhoan9.firstmvvm.data.response.StatusResponse;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.services.UserServices;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;

import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by hoang on 27/03/2017.
 */

public class UserRepoImpl implements UserRepo {
    Retrofit retrofit;
    UserServices services;

    public UserRepoImpl(Retrofit retrofit) {
        this.retrofit = retrofit;
        services = retrofit.create(UserServices.class);
    }

    @Override
    public Observable<Response<User>> register(UserRegisterParam params) {
        return Observable.create(subscriber -> {
            Observable<Response<User>> userResponse =
                    services.postRegisterUser(params);
            userResponse.compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<Response<User>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                            Log.d("Error", "UserrepoImpl");
                        }

                        @Override
                        public void onNext(Response<User> userResponse) {
                            subscriber.onNext(userResponse);
                        }
                    });
        });
    }

    @Override
    public Observable<Response<User>> getUserProfile() {
        return Observable.create(subscriber -> {
            Observable<Response<User>> userResponse = services.getUserProfile(1);
            userResponse.compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<Response<User>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                            Log.d("Error", "UserrepoImpl-getprofile");
                        }

                        @Override
                        public void onNext(Response<User> userResponse) {
                            subscriber.onNext(userResponse);
                        }
                    });
        });
    }

    @Override
    public Observable<Response<User>> loginByUsername(LoginUserParam params) {
        return Observable.create(subscriber -> {
            Observable<Response<User>> loginResponse =
                    services.loginByEmail(params);
            loginResponse.compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<Response<User>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                            Log.d("Error", "UserrepoImpl-loginByEmail");
                        }

                        @Override
                        public void onNext(Response<User> userResponse) {
                            subscriber.onNext(userResponse);
                        }
                    });
        });
    }

    @Override
    public Observable<Void> logout(int id) {
        return Observable.create(subscriber -> {
            services.logout(id)
                    .compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<Response<SessionDeleteResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(Response<SessionDeleteResponse> sessionDeleteResponseResponse) {
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        }
                    });
        });
    }

    @Override
    public Observable<NotificationResponse> registerRegToken(String regToken) {
        return services.registerRegToken(regToken);
    }

    @Override
    public Observable<Balance> getBalance() {
        return services.getBalance();
    }

    @Override
    public Observable<Balance> addVoucher(String code) {
        return services.addVoucher(code);
    }

    @Override
    public Observable<StatusResponse> checkVoucherCode(String code) {
        return services.checkBalanceCode(code);
    }

}
