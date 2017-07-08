package com.nux.dhoan9.firstmvvm.services;

import com.nux.dhoan9.firstmvvm.data.request.PaypalPartialReq;
import com.nux.dhoan9.firstmvvm.data.response.FullRefundResponse;
import com.nux.dhoan9.firstmvvm.data.response.PartialRefundResponse;
import com.nux.dhoan9.firstmvvm.data.response.PaypalCredentialResponse;
import com.nux.dhoan9.firstmvvm.data.response.PaypalDetailResponse;
import com.nux.dhoan9.firstmvvm.data.response.PaypalInfoDetailPayer;
import com.nux.dhoan9.firstmvvm.data.response.PaypalInvoiceInfoResponse;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by hoang on 03/07/2017.
 */

public interface IPaypalApi {

    @POST("oauth2/token")
    @FormUrlEncoded
    Observable<PaypalCredentialResponse> getCredential(@Field("grant_type") String grantType,
                                                       @Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @POST("payments/sale/{id}/refund")
    Observable<PartialRefundResponse> partialRefund(@Header("Authorization") String accessToken,
                                                    @Path("id") String id,
                                                    @Body String partialReq);

    @Headers("Content-Type: application/json")
    @POST("payments/sale/{id}/refund")
    Observable<FullRefundResponse> fullRefund(@Header("Authorization") String accessToken,
                                              @Path("id") String id,
                                              @Body String partialReq);

    @Headers("Content-Type: application/json")
    @GET("payments/payment/{id}")
    Observable<PaypalInvoiceInfoResponse> getInvoiceNumber(@Header("Authorization") String accessToken,
                                                           @Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("payments/payment/{id}/")
    Observable<PaypalDetailResponse> getPaymentDetail(@Header("Authorization") String accessToken,
                                                      @Path("id") String id);
}
