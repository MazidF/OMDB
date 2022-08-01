package com.example.omdb.ui.fragment.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.omdb.utils.bindable.BindableFactory

class ItemPagingAdapter<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val bindableFactory: BindableFactory<T>,
    private val onItemClick: (T) -> Unit,
) : PagingDataAdapter<T, ItemHolder<T>>(diffCallback) {


    override fun onBindViewHolder(holder: ItemHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder<T> {
        return ItemHolder(bindableFactory.inflate(parent), onItemClick)
    }
}
