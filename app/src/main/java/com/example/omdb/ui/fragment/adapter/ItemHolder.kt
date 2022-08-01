package com.example.omdb.ui.fragment.adapter

import android.view.View
import com.example.omdb.utils.bindable.Bindable
import com.example.omdb.utils.bindable.BindableHolder

class ItemHolder<T : Any>(
    bindable: Bindable<T>,
    onItemClick: (View, T) -> Unit,
) : BindableHolder<T>(bindable) {
    private var t: T? = null

    init {
        bindable.getView().apply {
            setOnClickListener {
                t?.let {
                    onItemClick(this, it)
                }
            }
        }
    }

    override fun bind(item: T?) {
        super.bind(item)
        t = item
    }
}