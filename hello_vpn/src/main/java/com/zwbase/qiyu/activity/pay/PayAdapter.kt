/*
 * Copyright © 2017-2019 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package com.zwbase.qiyu.activity.pay

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.zwbase.qiyu.BR

/**
 * A generic `RecyclerView.Adapter` backed by a `ObservableKeyedArrayList`.
 */
class PayAdapter<K> internal constructor(
        context: Context, private val layoutId: Int,
        list: List<K>?
) : RecyclerView.Adapter<PayAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var list: List<K>? = null
    private var rowConfigurationHandler: RowConfigurationHandler<ViewDataBinding, Any>? = null

    private fun getItem(position: Int): K? = if (list == null || position < 0 || position >= list!!.size) null else list?.get(position)

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.setVariable(BR.item, getItem(position))
        holder.binding.executePendingBindings()
        if (rowConfigurationHandler != null) {
            val item = getItem(position)
            if (item != null) {
                rowConfigurationHandler?.onConfigureRow(holder.binding, item, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(DataBindingUtil.inflate(layoutInflater, layoutId, parent, false))

    fun setList(newList: List<K>?) {
        list = newList
        notifyDataSetChanged()
    }

    fun setRowConfigurationHandler(rowConfigurationHandler: RowConfigurationHandler<*, *>?) {
        @Suppress("UNCHECKED_CAST")
        this.rowConfigurationHandler = rowConfigurationHandler as? RowConfigurationHandler<ViewDataBinding, Any>
    }

    interface RowConfigurationHandler<B : ViewDataBinding, T> {
        fun onConfigureRow(binding: B, item: T, position: Int)
    }


    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    init {
        setList(list)
    }
}
