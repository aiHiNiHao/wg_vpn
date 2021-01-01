package com.zwbase.qiyu.http.model;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/EasyHttp
 *    time   : 2019/05/19
 *    desc   : 统一接口数据结构
 */
public class HttpData<T> {

    /** 返回码 */
    private String code;
    /** 数据 */
    private T message;

    public String getCode() {
        return code;
    }

    public T getMessage() {
        return message;
    }
}
