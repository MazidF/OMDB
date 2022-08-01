package com.example.omdb.ui.fragment.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.omdb.utils.bindable.BindableFactory

class ItemListAdapter<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val bindableFactory: BindableFactory<T>,
    private val onItemClick: (T) -> Unit,
) : ListAdapter<T, ItemHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder<T> {
        val binding = bindableFactory.inflate(parent)
        return ItemHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ItemHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }
}