package com.nux.dhoan9.firstmvvm.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
/**
 * Created by hoang on 29/04/2017.
 */

public class ErrorUtils {
    public static <T> T parseError(Response<?> response, Class<T> clazz)
            throws IllegalAccessException, InstantiationException {
        Converter<ResponseBody, T> converter =
                (new RetrofitUtils(null)).create()
                        .responseBodyConverter(clazz, new Annotation[0]);
        T error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return clazz.newInstance();
        }

        return error;
    }
}
