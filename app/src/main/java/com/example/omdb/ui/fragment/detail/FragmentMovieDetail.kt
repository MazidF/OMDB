package com.example.omdb.ui.fragment.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.omdb.R
import com.example.omdb.data.result.Result
import com.example.omdb.databinding.FragmentMovieDetailBinding
import com.example.omdb.utils.launch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FragmentMovieDetail : Fragment(R.layout.fragment_movie_detail) {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding: FragmentMovieDetailBinding get() = _binding!!

    private val viewModel: ViewModelMovieDetail by viewModels()
    private val args: FragmentMovieDetailArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailBinding.bind(view)

        viewModel.getDetail(args.movie.id)
        initView()
        observe()
    }

    private fun initView() = with(binding) {

    }

    private fun observe() = with(binding) {
        launch {
            viewModel.movieStateFlow.collectLatest {
                when(it) {
                    is Result.Fail -> TODO()
                    is Result.Loading -> TODO()
                    is Result.Success -> TODO()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}