package com.nux.dhoan9.firstmvvm.services;

import com.nux.dhoan9.firstmvvm.data.response.SessionDeleteResponse;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.Constant;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
/**
 * Created by hoang on 29/04/2017.
 */

public interface UserServices {

    @FormUrlEncoded
    @POST("api/users")
    Observable<Response<User>> postRegisterUser(@Field(Constant.FIRST_NAME_PARAMS) String firstName,
                                                @Field(Constant.LAST_NAME_PARAMS) String lastName,
                                                @Field(Constant.EMAIL_PARAMS) String email,
                                                @Field(Constant.PASSWORD_PARAMS) String password,
                                                @Field(Constant.PASSWORD_CONFIRMATION_PARAMS) String passwordConfirmation);

    @GET("api/users/1")
    Observable<Response<User>> getUserProfile();

    @FormUrlEncoded
    @POST("api/sessions")
    Observable<Response<User>> loginByEmail(@Field(Constant.EMAIL_PARAMS) String email,
                                            @Field(Constant.PASSWORD_PARAMS) String password);

    @DELETE("api/sessions/{id}")
    Observable<Response<SessionDeleteResponse>> logout(@Path("id") int id);
}
