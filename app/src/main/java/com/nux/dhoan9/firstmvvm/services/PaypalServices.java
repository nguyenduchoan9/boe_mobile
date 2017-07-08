package com.nux.dhoan9.firstmvvm.services;

import com.nux.dhoan9.firstmvvm.data.response.FullRefundResponse;
import com.nux.dhoan9.firstmvvm.data.response.PartialRefundResponse;
import com.nux.dhoan9.firstmvvm.data.response.PaypalCredentialResponse;
import com.nux.dhoan9.firstmvvm.data.response.PaypalDetailResponse;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by hoang on 03/07/2017.
 */

public class PaypalServices {
    private static Retrofit api = RetrofitUtils.create("https://api.sandbox.paypal.com/v1/");
    private static IPaypalApi services = api.create(IPaypalApi.class);

    public static Observable<PaypalCredentialResponse> getCredential() {
        String authorization = Constant.PAYPAL_AUTHORIZRTION;
        return services.getCredential("client_credentials", authorization);
    }

    public static Observable<PartialRefundResponse> partialRefund(String id,
                                                                  String accessToken,
                                                                  String total) {
        String authorization = "Bearer " + accessToken;
        JSONObject params = new JSONObject();
        JSONObject amount = new JSONObject();
        try {
            amount.put("total", total);
            amount.put("currency", "USD");
            params.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        String paramsRaw = "{\"amount\":{\"total\":\"XXX_PARAMS\",\"currency\":\"USD\"}}";
//        paramsRaw = paramsRaw.replace("XXX_PARAMS", total);
//        String bodyParams = body.replace("total_param", total);
        return services.partialRefund(authorization, id, params.toString());
    }

    public static Observable<FullRefundResponse> fullRefund(String id,
                                                            String accessToken) {
        String authorization = "Bearer " + accessToken;
        JSONObject params = new JSONObject();
        return services.fullRefund(authorization, id, params.toString());
    }

    public static Observable<PaypalDetailResponse> getDetailByPaymentId(String id,
                                                                        String accessToken) {
        String authorization = "Bearer " + accessToken;
        return services.getPaymentDetail(authorization, id);
    }
}
