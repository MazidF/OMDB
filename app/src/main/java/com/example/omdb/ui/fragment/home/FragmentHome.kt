package com.example.omdb.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.omdb.R
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.databinding.FragmentHomeBinding
import com.example.omdb.databinding.LoadStateItemBinding
import com.example.omdb.ui.fragment.FragmentWithLottie
import com.example.omdb.ui.fragment.home.adapter.ItemPagingAdapter
import com.example.omdb.ui.fragment.home.adapter.SmallMovieItemFactory
import com.example.omdb.ui.fragment.home.adapter.diffcallback.MovieDiffCallback
import com.example.omdb.ui.fragment.home.adapter.loading.LoadingStateAdapter
import com.example.omdb.utils.isLandscape
import com.example.omdb.utils.launch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FragmentHome : FragmentWithLottie(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val navController by lazy {
        findNavController()
    }
    private val viewModel: ViewModelHome by activityViewModels()
    private lateinit var movieAdapter: ItemPagingAdapter<Movie>

    override fun onViewCreated(view: View) {
        _binding = FragmentHomeBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() {
        setupListSpanCount()
        setupAdapter()
    }

    private fun setupListSpanCount() = with(binding) {
        (list.layoutManager as GridLayoutManager).spanCount = if (requireContext().isLandscape()) {
            6
        } else {
            3
        }
    }

    private fun setupAdapter() = with(binding) {
        movieAdapter = ItemPagingAdapter(
            diffCallback = MovieDiffCallback(),
            bindableFactory = SmallMovieItemFactory(),
            onItemClick = this@FragmentHome::onItemClick,
        )

        list.adapter = movieAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter(movieAdapter::retry, loading),
        )
    }

    private fun onItemClick(item: Movie) {
        navController.navigate(
            FragmentHomeDirections.actionFragmentHomeToFragmentMovieDetail(item)
        )
    }

    private fun observe() = with(binding) {
        launch {
            viewModel.pagingFlow.collect {
                movieAdapter.submitData(it)
            }
        }

        launch {
            movieAdapter.loadStateFlow.collectLatest { state ->
                val refresh = state.refresh
                val append = state.append
                setupLoadState(refresh, append)
            }
        }
    }

    private fun setupLoadState(refresh: LoadState, append: LoadState) = binding.loading.apply {
        if (refresh !is LoadState.Loading) {
            stopAnimation()
        } else {
            startAnimation()
        }
        val isError = (append is LoadState.Error) or (refresh is LoadState.Error)
        errorRoot.isVisible = isError
        progressbar.isVisible = append is LoadState.Loading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
