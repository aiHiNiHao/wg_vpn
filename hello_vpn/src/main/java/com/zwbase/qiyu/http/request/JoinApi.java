package com.zwbase.qiyu.http.request;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

/**
 *    desc   : 获取配置
 */
public final class JoinApi implements IRequestApi {

    @Override
    public String getApi() {
        return "vpn/join";
    }

    @HttpHeader
    private String userIdentity;

    private String channel;

    public JoinApi setUserIdentity(final String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

    public JoinApi setChannel(final String channel) {
        this.channel = channel;
        return this;
    }
}