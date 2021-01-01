/*
 * Copyright © 2020 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.zwbase.qiyu.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import kotlinx.coroutines.GlobalScope;

import com.hjq.http.EasyHttp;
import com.hjq.toast.ToastUtils;
import com.wireguard.android.backend.Tunnel.State;
import com.zwbase.qiyu.Application;
import com.zwbase.qiyu.R;
import com.zwbase.qiyu.activity.BaseActivity;
import com.zwbase.qiyu.activity.pay.PayListActivity;
import com.zwbase.qiyu.http.callback.DefultHttpCallback;
import com.zwbase.qiyu.http.model.HttpData;
import com.zwbase.qiyu.http.request.BreakApi;
import com.zwbase.qiyu.http.request.JoinApi;
import com.zwbase.qiyu.http.response.JoinBean;
import com.wireguard.android.backend.GoBackend;
import com.wireguard.android.backend.Tunnel;
import com.wireguard.android.model.ObservableTunnel;
import com.wireguard.config.BadConfigException;;
import com.wireguard.config.Config;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringBufferInputStream;


public class MainActivity extends BaseActivity {
    private final int VPN_REQUEST_CODE = 0x0F;
    ObservableTunnel tunnel;


    @Override public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SwitchCompat switcher = findViewById(R.id.toggle);
        switcher.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startVPN();
            } else {
                new Thread(() -> {
                    try {
                        Application.getBackend().setState(tunnel, State.DOWN, tunnel.getConfig());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        Button btn_buy = findViewById(R.id.btn_buy);
        btn_buy.setOnClickListener(view -> {
            startActivity(new Intent(this, PayListActivity.class));
        });
    }


    private void getConfig() {

        EasyHttp.post(this)
                .api(new JoinApi()
                        .setUserIdentity("123456")
                        .setChannel("渠道包"))
                .request(new DefultHttpCallback<HttpData<JoinBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<JoinBean> data) {
                        Config config = createConfig(data.getMessage());
                        tunnel = new ObservableTunnel("this", config, Tunnel.State.UP) {
                            @Override public void onStateChange(@NotNull final State newState) {

                            }
                        };
                        new Thread(() -> {
                            try {
                                Application.getBackend().setState(tunnel, State.UP, tunnel.getConfig());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }

                    @Override public void onCodeFail(final String code) {
                        if (TextUtils.equals("8000", code)) {
                            ToastUtils.show("会员到期，请先充值");
                        }
                    }
                });

    }

    private Config createConfig(JoinBean joinBean) {
        String configText =
                "[Interface]\n" +
                        "PrivateKey = " + joinBean.getPrivateKey() + "\n" +
                        "Address = " + joinBean.getAddress() + "\n" +
                        "DNS = " + joinBean.getDns() + "\n" +
                        "\n" +
                        "[Peer]\n" +
                        "PublicKey = " + joinBean.getPublicKey() + "\n" +
                        "Endpoint = " + joinBean.getEndPoint() + "\n" +
                        "AllowedIPs = " + joinBean.getAllowedIPs();

        Config config = null;
        try {
            config = Config.parse(new StringBufferInputStream(configText));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadConfigException e) {
            e.printStackTrace();
        }
        return config;
    }

    private void breakConnect() {
        EasyHttp.post(this)
                .api(new BreakApi()
                        .setUserIdentity("123456"))
                .request(new DefultHttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> data) {
                        ToastUtils.show("断开");
                    }

                });
    }

    @Override protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VPN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getConfig();
        }
    }

    private void startVPN() {
        Intent vpnIntent = GoBackend.VpnService.prepare(this);
        if (vpnIntent != null) {
            startActivityForResult(vpnIntent, VPN_REQUEST_CODE);
        } else {
            onActivityResult(VPN_REQUEST_CODE, Activity.RESULT_OK, null);
        }
    }
}