/*
 * Copyright © 2020 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.zwbase.qiyu.wxapi

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.hjq.toast.ToastUtils
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.zwbase.qiyu.BuildConfig


class WXPayEntryActivity : Activity(), IWXAPIEventHandler {

    private lateinit var api: IWXAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, "wxfc32f3770ec3936f")
        api.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq?) {}

    override fun onResp(resp: BaseResp) {
        if (BuildConfig.DEBUG)
            Log.d("TAG", "onPayFinish, errCode = " + resp.errCode)
//        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("R.string.app_tip")
//            builder.setMessage("pay_result_callback_msg")
//            builder.show()
//        }
        ToastUtils.show(if (resp.errCode == 0) "支付成功" else "支付失败")
        finish()
    }

    companion object {
        private const val TAG = "MicroMsg.SDKSample.WXPayEntryActivity"
    }
}