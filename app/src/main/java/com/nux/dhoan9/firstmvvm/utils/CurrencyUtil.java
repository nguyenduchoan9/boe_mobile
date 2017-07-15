package com.nux.dhoan9.firstmvvm.utils;

import com.nux.dhoan9.firstmvvm.model.Currency;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hoang on 29/06/2017.
 */

public class CurrencyUtil {
    private static Retrofit create() {
        return new Retrofit.Builder()
                .baseUrl("http://apilayer.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static Observable<Float> convertVNDToUSD(float vnd) {
        CurrencyService service = create().create(CurrencyService.class);
        return service.getCurrencyRate()
                .compose(RxUtils.onProcessRequest())
                .map(currency -> currency.convertVNDtoUSD(vnd));
    }

    public interface CurrencyService {
        @GET("live?access_key=088bcad87c3fd862b4793c2f535089ba")
        Observable<Currency> getCurrencyRate();
    }

    public static String formatVNDecimal(float money) {
        Locale local = new Locale("vi","VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(local);
//        DecimalFormat decimalFormat = new DecimalFormat("###.###.###");
        return currencyFormat.format(money);
    }
}
