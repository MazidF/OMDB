package com.example.omdb.ui.fragment.home

import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.omdb.R
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.databinding.FragmentHomeBinding
import com.example.omdb.databinding.SmallMovieItemBinding
import com.example.omdb.ui.fragment.FragmentWithLottie
import com.example.omdb.ui.fragment.adapter.ItemPagingAdapter
import com.example.omdb.ui.fragment.adapter.diffcallback.MovieDiffCallback
import com.example.omdb.ui.fragment.adapter.factory.SmallMovieItemFactory
import com.example.omdb.ui.fragment.adapter.loading.LoadingStateAdapter
import com.example.omdb.utils.helper.ConnectionHelper
import com.example.omdb.utils.isLandscape
import com.example.omdb.utils.launch
import dagger.hilt.android.AndroidEntryPoint
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

        setupAnimation(view)
        initView()
        observe()
    }

    private fun setupAnimation(view: View) {
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
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

    private fun onItemClick(view: View, item: Movie) {
        val imageView = SmallMovieItemBinding.bind(view).image
        val extras = FragmentNavigatorExtras(
            imageView to item.title
        )
        navController.navigate(
            FragmentHomeDirections.actionFragmentHomeToFragmentMovieDetail(item, item.title),
            navigatorExtras = extras,
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

        viewModel.connectionState.observe(viewLifecycleOwner) {
            if (it.isConnected()) {
                movieAdapter.refresh()
            }
            if (it != ConnectionHelper.ConnectionState.NONE) {
                viewModel.resetConnectionState()
            }
        }
    }

    private fun setupLoadState(refresh: LoadState, append: LoadState) = binding.loading.apply {
        errorRoot.isVisible = append is LoadState.Error
        progressbar.isVisible = append is LoadState.Loading

        when (refresh) {
            is LoadState.NotLoading -> {
                stopAnimation()
            }
            is LoadState.Error -> {
                if (movieAdapter.itemCount == 0) {
                    showError(movieAdapter::retry)
                } else {
                    errorRoot.isVisible = true
                }
            }
            is LoadState.Loading -> {
                if (movieAdapter.itemCount == 0) {
                    startAnimation()
                } else {
                    progressbar.isVisible = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
