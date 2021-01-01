/*
 * Copyright © 2020 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.zwbase.qiyu.http.response

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

data class OrderBean(val appid :String, val noncestr :String, val `package` :String, val partnerid :String, var prepayid :String,val sign:String, val timestamp:String):BaseObservable(){

}