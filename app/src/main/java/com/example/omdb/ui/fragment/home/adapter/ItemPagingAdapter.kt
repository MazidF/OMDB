package com.example.omdb.ui.fragment.home.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.omdb.utils.bindable.Bindable
import com.example.omdb.utils.bindable.BindableFactory
import com.example.omdb.utils.bindable.BindableHolder

class ItemPagingAdapter<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val bindableFactory: BindableFactory<T>,
    private val onItemClick: (T) -> Unit,
) : PagingDataAdapter<T, ItemPagingAdapter<T>.ItemHolder>(diffCallback) {

    inner class ItemHolder(
        bindable: Bindable<T>
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

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(bindableFactory.inflate(parent))
    }
}

