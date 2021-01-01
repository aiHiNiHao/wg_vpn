package com.zwbase.qiyu.http.request;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

/**
 *    desc   : 获取配置
 */
public final class PayListApi implements IRequestApi {

    @Override
    public String getApi() {
        return "vpn/commodity/list";
    }


}