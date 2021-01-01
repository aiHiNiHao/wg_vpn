/*
 * Copyright © 2017-2019 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package com.wireguard.android.model

import com.wireguard.android.backend.Tunnel
import com.wireguard.config.Config

/**
 * Encapsulates the volatile and nonvolatile state of a WireGuard tunnel.
 */
open class ObservableTunnel internal constructor(
        private var name: String,
        var config: Config,
        var state: Tunnel.State
) :  Tunnel {

    override fun getName() = name

    override fun onStateChange(newState: Tunnel.State) {
    }
    companion object {
        private const val TAG = "WireGuard/ObservableTunnel"
    }
}
