package com.example.omdb.utils.bindable

import androidx.recyclerview.widget.RecyclerView

open class BindableHolder<T>(
    private val bindable: Bindable<T>
) : RecyclerView.ViewHolder(bindable.getView()) {
    open fun bind(item: T?) {
        item?.let {
            bindable.bind(it)
        }
    }
}