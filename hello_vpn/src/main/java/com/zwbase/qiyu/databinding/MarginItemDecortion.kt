/*
 * Copyright © 2020 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.zwbase.qiyu.databinding

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecortion : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        if (itemPosition % 2 == 0){
            outRect.set(0, 20, 10, 0)
        }else{
            outRect.set(10, 20, 0, 0)
        }
    }
}
