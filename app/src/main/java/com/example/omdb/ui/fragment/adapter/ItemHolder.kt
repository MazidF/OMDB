package com.example.omdb.ui.fragment.adapter

import com.example.omdb.utils.bindable.Bindable
import com.example.omdb.utils.bindable.BindableHolder

class ItemHolder<T : Any>(
    bindable: Bindable<T>,
    onItemClick: (T) -> Unit,
) : BindableHolder<T>(bindable) {
    private var t: T? = null

    init {
        bindable.getView().setOnClickListener {
            t?.let(onItemClick)
        }
    }

    override fun bind(item: T?) {
        super.bind(item)
        t = item
    }
}