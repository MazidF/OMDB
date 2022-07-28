package com.example.omdb.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.omdb.R
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.databinding.FragmentHomeBinding
import com.example.omdb.ui.fragment.home.adapter.ItemPagingAdapter
import com.example.omdb.ui.fragment.home.adapter.SmallMovieItemFactory
import com.example.omdb.ui.fragment.home.adapter.diffcallback.MovieDiffCallback
import com.example.omdb.utils.launch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class FragmentHome : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val viewModel: ViewModelHome by viewModels()
    private lateinit var movieAdapter: ItemPagingAdapter<Movie>

    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        movieAdapter = ItemPagingAdapter(
            diffCallback = MovieDiffCallback(),
            bindableFactory = SmallMovieItemFactory(),
            onItemClick = this@FragmentHome::onItemClick,
        )

        list.adapter = movieAdapter
    }

    private fun onItemClick(item: Movie) {

    }

    private fun observe() = with(binding) {
        job = launch {
            viewModel.search("Batman").collect {
                movieAdapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
