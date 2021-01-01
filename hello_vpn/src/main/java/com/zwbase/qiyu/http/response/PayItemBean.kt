/*
 * Copyright © 2020 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.zwbase.qiyu.http.response

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

data class PayItemBean(@get:Bindable val id :String, @get:Bindable val name :String, @get:Bindable val price :Float, @get:Bindable val dayPrice :Float, @get:Bindable var checked :Boolean):BaseObservable(){

}