package com.example.omdb.ui.fragment.home.adapter.loading

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.omdb.databinding.LoadStateItemBinding

class LoadingStateAdapter(
    private val onRetryClick: () -> Unit,
    private val binding: LoadStateItemBinding,
) : LoadStateAdapter<LoadStateHolder>() {

    override fun onBindViewHolder(holder: LoadStateHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateHolder {
        return LoadStateHolder(binding, onRetryClick)
    }
}