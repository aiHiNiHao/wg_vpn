package com.zwbase.qiyu.http.request;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

/**
 *    desc   : 获取配置
 */
public final class BreakApi implements IRequestApi {

    @Override
    public String getApi() {
        return "vpn/cancel";
    }

    @HttpHeader
    private String userIdentity;


    public BreakApi setUserIdentity(final String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

}