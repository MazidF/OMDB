package com.example.omdb.utils.bindable

import android.view.View

interface Bindable<T> {
    fun bind(item: T)
    fun getView(): View
    fun setOnClickListener(l: View.OnClickListener?)
}