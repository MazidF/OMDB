package com.example.omdb.ui.fragment.adapter.loading

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.omdb.databinding.LoadStateItemBinding

class LoadStateHolder(
    private val binding: LoadStateItemBinding,
    private val onRetryClick: () -> Unit,
) : RecyclerView.ViewHolder(View(binding.root.context)) {

    init {
        binding.apply {
            retry.setOnClickListener {
                onRetryClick()
            }
        }
    }

    fun bind(loadState: LoadState) = with(binding) {
        Log.d("loadState", loadState.toString())
        val isError = loadState is LoadState.Error
        errorRoot.isVisible = isError
        progressbar.isVisible = loadState is LoadState.Loading
    }
}
