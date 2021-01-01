/*
 * Copyright © 2020 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.zwbase.qiyu.http.callback;

import com.hjq.http.listener.OnHttpListener;
import com.zwbase.qiyu.http.error.CodeException;

import okhttp3.Call;

/**
 *    time   : 2020/12/20
 *    desc   : 请求回调包装类
 */
public class DefultHttpCallback<T> implements OnHttpListener<T> {

    private final OnHttpListener mListener;

    public DefultHttpCallback(OnHttpListener listener) {
        mListener = listener;
    }

    @Override
    public void onStart(Call call) {
        if (mListener != null) {
            mListener.onStart(call);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSucceed(T result) {
        if (mListener != null) {
            mListener.onSucceed(result);
        }
    }

    @Override
    public void onFail(Exception e) {
        if (e instanceof CodeException){
            CodeException error = (CodeException) e;
            onCodeFail(error.getCode());
        }else{
            if (mListener != null) {
                mListener.onFail(e);
            }
        }
    }

    public void onCodeFail(String code){

    }

    @Override
    public void onEnd(Call call) {
        if (mListener != null) {
            mListener.onEnd(call);
        }
    }
}
