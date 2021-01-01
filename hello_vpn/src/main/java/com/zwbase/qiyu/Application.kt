/*
 * Copyright © 2017-2019 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package com.zwbase.qiyu

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.util.Log
import com.hjq.http.EasyConfig
import com.hjq.http.config.IRequestServer
import com.hjq.toast.ToastUtils
import com.zwbase.qiyu.http.model.RequestHandler
import com.zwbase.qiyu.http.server.ReleaseServer
import com.wireguard.android.backend.Backend
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.util.ModuleLoader
import com.wireguard.android.util.RootShell
import com.wireguard.android.util.ToolsInstaller
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import java.util.Locale

class Application : android.app.Application() {
    private val futureBackend = CompletableDeferred<Backend>()
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main.immediate)
    private lateinit var backend: Backend
    private lateinit var moduleLoader: ModuleLoader
    private lateinit var rootShell: RootShell
    private lateinit var toolsInstaller: ToolsInstaller

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        if (BuildConfig.MIN_SDK_VERSION > Build.VERSION.SDK_INT) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            System.exit(0)
        }
        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(VmPolicy.Builder().detectAll().penaltyLog().build())
            StrictMode.setThreadPolicy(ThreadPolicy.Builder().detectAll().penaltyLog().build())
        }
    }


    override fun onCreate() {
        Log.i(TAG, USER_AGENT)
        super.onCreate()
        rootShell = RootShell(applicationContext)
        toolsInstaller = ToolsInstaller(applicationContext, rootShell)
        moduleLoader = ModuleLoader(applicationContext, rootShell, USER_AGENT)
        backend = GoBackend(applicationContext)

        ToastUtils.init(this);

        // 网络请求框架初始化
        val server : IRequestServer = if (BuildConfig.DEBUG) {
            ReleaseServer()
        } else {
             ReleaseServer()
        }

        val okHttpClient =  OkHttpClient.Builder()
                .build();

        EasyConfig.with(okHttpClient)
                // 是否打印日志
                .setLogEnabled(BuildConfig.DEBUG)
                // 设置服务器配置
                .setServer(server)
                // 设置请求处理策略
                .setHandler( RequestHandler(this))
                // 设置请求参数拦截器
                .setInterceptor { url, tag, params, headers -> }
                // 设置请求重试次数
                .setRetryCount(1)
                // 设置请求重试时间
                .setRetryTime(1000)
                // 添加全局请求参数
                //.addParam("token", "6666666")
                // 添加全局请求头
                //.addHeader("time", "20191030")
                .into();

    }



    override fun onTerminate() {
        coroutineScope.cancel()
        super.onTerminate()
    }

    companion object {
        val USER_AGENT = String.format(Locale.ENGLISH, "WireGuard/%s (Android %d; %s; %s; %s %s; %s)", BuildConfig.VERSION_NAME, Build.VERSION.SDK_INT, if (Build.SUPPORTED_ABIS.isNotEmpty()) Build.SUPPORTED_ABIS[0] else "unknown ABI", Build.BOARD, Build.MANUFACTURER, Build.MODEL, Build.FINGERPRINT)
        private const val TAG = "WireGuard/Application"
        private lateinit var weakSelf: WeakReference<Application>

        @JvmStatic
        fun get(): Application {
            return weakSelf.get()!!
        }

        @JvmStatic
         fun getBackend() = get().backend

        @JvmStatic
        fun getModuleLoader() = get().moduleLoader

        @JvmStatic
        fun getRootShell() = get().rootShell

        @JvmStatic
        fun getToolsInstaller() = get().toolsInstaller

        @JvmStatic
        fun getCoroutineScope() = get().coroutineScope
    }

    init {
        weakSelf = WeakReference(this)
    }
}
