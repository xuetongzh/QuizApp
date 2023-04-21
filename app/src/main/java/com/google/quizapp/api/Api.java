package com.google.quizapp.api;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.quizapp.bean.Question;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author : lancer 🐢
 * e-mail : lancer2ooo@qq.com
 * desc   : hello word
 */
public class Api {
    public static String API_BASE_URL = "https://apis.tianapi.com/scwd/index";
    public static final Api INSTANCE = new Api();

    private final OkHttpClient mClient = new OkHttpClient.Builder()
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public void get(Callback<ApiResult<Question>> callback) {
        FormBody formBody = new FormBody.Builder()
                .add("key", "63596c45af26d3a8dcc03f57fdca5ddb")
                .build();
        Gson gson = new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
        Request request = new Request.Builder().url(API_BASE_URL).post(formBody).build();

        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.result(false, 500, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    ResponseBody responseBody = response.body();
                    ApiResult<Question> apiResult = gson.fromJson(responseBody.string(), new TypeToken<ApiResult<Question>>() {
                    }.getType());
                    callback.result(true, response.code(), apiResult);
                } else {
                    callback.result(false, response.code(), null);
                }
            }
        });
    }

    public interface Callback<T> {
        void result(boolean success, int errCode, @Nullable T data);
    }

    /**
     * GET Request to add parameters
     */
    private String getBodyParams(Map<String, String> bodyParams) {
        //1.Request to add parameters
        //Traverse all parameters in the map to the builder
        if (bodyParams != null && bodyParams.size() > 0) {
            StringBuilder stringBuffer = new StringBuilder("?");
            for (String key : bodyParams.keySet()) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key)) && stringBuffer.length() > 2) {
                    //Concatenate them if the parameters are not null and not ""
                    stringBuffer.append("&");
                    stringBuffer.append(key);
                    stringBuffer.append("=");
                    stringBuffer.append(bodyParams.get(key));
                } else if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
                    stringBuffer.append(key);
                    stringBuffer.append("=");
                    stringBuffer.append(bodyParams.get(key));
                }
            }

            return stringBuffer.toString();
        } else {
            return "";
        }
    }
}
