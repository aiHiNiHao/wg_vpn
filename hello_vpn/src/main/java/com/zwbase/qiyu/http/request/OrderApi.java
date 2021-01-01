package com.zwbase.qiyu.http.request;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

/**
 *    desc   : 获取配置
 */
public final class OrderApi implements IRequestApi {

    @Override
    public String getApi() {
        return "vpn/order/wxpayInfo";
    }

    @HttpHeader
    private String userIdentity;

    private String commodityId;

    public OrderApi setUserIdentity(final String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

    public OrderApi setCommodityId(final String commodityId) {
        this.commodityId = commodityId;
        return this;
    }
}