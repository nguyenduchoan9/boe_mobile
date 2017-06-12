package com.nux.dhoan9.firstmvvm.services;

import com.nux.dhoan9.firstmvvm.data.request.user.LoginUserParam;
import com.nux.dhoan9.firstmvvm.data.request.user.UserRegisterParam;
import com.nux.dhoan9.firstmvvm.data.response.NotificationResponse;
import com.nux.dhoan9.firstmvvm.data.response.SessionDeleteResponse;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.Constant;

import retrofit2.Response;
import retrofit2.http.Body;
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

//    @FormUrlEncoded
    @POST("users")
    Observable<Response<User>> postRegisterUser(@Body UserRegisterParam param);

    @GET("users/{id}")
    Observable<Response<User>> getUserProfile(@Path("id") long id);

//    @FormUrlEncoded
    @POST("sessions")
    Observable<Response<User>> loginByEmail(@Body LoginUserParam param);

    @DELETE("sessions/{id}")
    Observable<Response<SessionDeleteResponse>> logout(@Path("id") long id);

    @POST("notifications/register_reg_token")
    @FormUrlEncoded
    Observable<NotificationResponse> registerRegToken(@Field("reg_token") String regToken);
}
