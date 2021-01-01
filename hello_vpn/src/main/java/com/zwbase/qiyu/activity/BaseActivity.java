/*
 * Copyright © 2020 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.zwbase.qiyu.activity;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.zwbase.qiyu.BuildConfig;
import com.zwbase.qiyu.R;
import com.zwbase.qiyu.http.model.HttpData;

import okhttp3.Call;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/EasyHttp
 * time   : 2019/05/19
 * desc   : 基类封装
 */
public class BaseActivity extends AppCompatActivity implements OnHttpListener {

    /**
     * 加载对话框
     */
    private ProgressDialog mDialog;
    /**
     * 对话框数量
     */
    private int mDialogTotal;

    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        return mDialog != null && mDialog.isShowing();
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage(getResources().getString(R.string.http_loading));
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mDialogTotal++;
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        if (mDialogTotal == 1) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
        if (mDialogTotal > 0) {
            mDialogTotal--;
        }
    }

    @Override
    public void onStart(Call call) {
        showDialog();
    }

    @Override
    public void onSucceed(Object result) {
        if (result instanceof HttpData) {
            if (BuildConfig.DEBUG)
                ToastUtils.show(((HttpData) result).getCode());
        }
    }

    @Override
    public void onFail(Exception e) {
        ToastUtils.show(e.getMessage());
    }

    public void onCodeFail(String code){

    }

    @Override
    public void onEnd(Call call) {
        hideDialog();
    }
}