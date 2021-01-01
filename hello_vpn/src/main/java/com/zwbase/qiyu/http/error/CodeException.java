/*
 * Copyright © 2020 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.zwbase.qiyu.http.error;

public class CodeException extends Exception{

    private String code;

    public CodeException(String message,String code){
        super(message);
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
