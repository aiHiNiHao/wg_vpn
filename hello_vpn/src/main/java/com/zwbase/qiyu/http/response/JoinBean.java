/*
 * Copyright © 2020 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.zwbase.qiyu.http.response;

public class JoinBean {
    
            private String id;// 21,
            private String userIdentity;// "123456",
            private String privateKey;// "0P9uEBnk5JmGSQlZw6khZqtA+te6bY4jZCT78JOLrGE=",
            private String publicKey;// "gd5KpFBKb7w3e7n7N9QPLx0cakiSDzPlN7BrwKV3TA8=",
            private String allowedIPs;// "0.0.0.0/0",
            private String dns;// "114.114.114.114",
            private String endPoint;// "192.144.214.243:54321",
            private String address;// "10.10.10.10"

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(final String userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAllowedIPs() {
        return allowedIPs;
    }

    public void setAllowedIPs(final String allowedIPs) {
        this.allowedIPs = allowedIPs;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(final String dns) {
        this.dns = dns;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(final String endPoint) {
        this.endPoint = endPoint;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }
}
