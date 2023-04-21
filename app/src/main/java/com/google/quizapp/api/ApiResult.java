package com.google.quizapp.api;

import androidx.annotation.NonNull;

/**
 * author : lancer 🐢
 * e-mail : lancer2ooo@qq.com
 * desc   : hello word
 */
public class ApiResult<T> {
    private int code;
    private String msg;
    private T result;

    @NonNull
    @Override
    public String toString() {
        return "ApiResult{" +
                "code=" + code +
                ", message='" + msg + '\'' +
                ", data=" + result +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public T getData() {
        return result;
    }

    public void setData(T data) {
        this.result = data;
    }
}
