/*
 * Copyright © 2020 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.zwbase.qiyu.activity.pay

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import com.hjq.http.EasyHttp
import com.hjq.toast.ToastUtils
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.zwbase.qiyu.BR
import com.zwbase.qiyu.activity.BaseActivity
import com.zwbase.qiyu.databinding.ActivityPayListBinding
import com.zwbase.qiyu.databinding.PayListItemBinding
import com.zwbase.qiyu.http.callback.DefultHttpCallback
import com.zwbase.qiyu.http.model.HttpData
import com.zwbase.qiyu.http.request.OrderApi
import com.zwbase.qiyu.http.request.PayListApi
import com.zwbase.qiyu.http.response.OrderBean
import com.zwbase.qiyu.http.response.PayItemBean


class PayListActivity : BaseActivity() {
    private lateinit var binding: ActivityPayListBinding
    private var payItemBean: PayItemBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvBuy.setOnClickListener { getPayOrder() }
        requestPayList()
    }

    private fun requestPayList() {
        EasyHttp.post(this)
                .api(PayListApi())
                .request(object : DefultHttpCallback<HttpData<List<PayItemBean>>>(this) {
                    override fun onSucceed(result: HttpData<List<PayItemBean>>?) {
                        binding.payItems = result?.message
                        binding!!.rowConfigurationHandler = object : PayAdapter.RowConfigurationHandler<PayListItemBinding, PayItemBean> {
                            override fun onConfigureRow(binding: PayListItemBinding, item: PayItemBean, position: Int) {
                                binding.root.isSelected = item.checked
                                val str = SpannableString("￥${item.price}")
                                val sizeSpan = AbsoluteSizeSpan(24, true)
                                str.setSpan(sizeSpan, 1, str.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                binding.packPrice.text = str
                                binding.root.setOnClickListener {
                                    payItemBean = item
                                    this@PayListActivity.binding.payItems?.forEachIndexed { index, payItemBean ->
                                        val old = payItemBean.checked
                                        payItemBean.checked = index == position
                                        if (old != payItemBean.checked) {
                                            this@PayListActivity.binding.payList.adapter?.notifyItemChanged(index)
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
    }

    private fun getPayOrder() {
        payItemBean?.let { item ->
            EasyHttp.post(this)
                    .api(OrderApi()
                            .setUserIdentity("123456")
                            .setCommodityId(item.id))
                    .request(object : DefultHttpCallback<HttpData<OrderBean>>(this) {
                        override fun onSucceed(result: HttpData<OrderBean>?) {
                            result?.message?.let { doPay(it) }
                        }
                    })
        }

    }

    private fun doPay(order: OrderBean) {
        try {
            val req = PayReq()
            //req.appId = "wxf8b4f85f3a794e77";  // ²âÊÔÓÃappId
            req.appId = order.appid
            req.partnerId = order.partnerid
            req.prepayId = order.prepayid
            req.nonceStr = order.noncestr
            req.timeStamp = order.timestamp
            req.packageValue = order.`package`
            req.sign = order.sign
            req.extData = "app data" // optional
            val api: IWXAPI = WXAPIFactory.createWXAPI(this, null);
            api.sendReq(req)

        } catch (e: Exception) {
            Log.e("PAY_GET", "Òì³££º" + e.message)
            ToastUtils.show(e.message)
        }
    }
}

