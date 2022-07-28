package com.example.omdb.utils.bindable

import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BindableFactory<T> {

    fun inflate(parent: ViewGroup, attachToParent: Boolean = false): Bindable<T> {
        return inflate(
            layoutInflater = LayoutInflater.from(parent.context),
            parent = parent,
            attachToParent = attachToParent,
        )
    }

    protected abstract fun inflate(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        attachToParent: Boolean
    ): Bindable<T>
}