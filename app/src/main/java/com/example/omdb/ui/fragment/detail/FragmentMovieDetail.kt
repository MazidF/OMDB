package com.example.omdb.ui.fragment.detail

import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.omdb.R
import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.result.Result
import com.example.omdb.databinding.FragmentMovieDetailBinding
import com.example.omdb.ui.fragment.RefreshableFragment
import com.example.omdb.utils.launch
import com.example.omdb.utils.loadImage
import com.example.omdb.utils.onBack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FragmentMovieDetail : RefreshableFragment(R.layout.fragment_movie_detail), MotionLayout.TransitionListener {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding: FragmentMovieDetailBinding get() = _binding!!

    private val viewModel: ViewModelMovieDetail by viewModels()
    private val args: FragmentMovieDetailArgs by navArgs()
    private val movie get() = args.movie

    override fun onChildViewCreated(view: View) {
        _binding = FragmentMovieDetailBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        refresh()
    }

    private fun observe() = with(binding) {
        launch {
            viewModel.movieStateFlow.collectLatest {
                when (it) {
                    is Result.Fail -> {
                        onFail()
                    }
                    is Result.Loading -> {
                        startAnimation()
                    }
                    is Result.Success -> {
                        onSuccess(it.data())
                    }
                }
            }
        }
        root.addTransitionListener(this@FragmentMovieDetail)
    }

    private fun onFail() {
        showError(this@FragmentMovieDetail::refresh)
    }

    override fun refresh() {
        viewModel.getDetail(movie.id)
    }

    private fun onSuccess(data: MovieDetailWithGenres) = with(binding) {
        setupView(data)
        stopAnimation()
        stopRefreshing()
    }

    private fun setupView(data: MovieDetailWithGenres) = with(binding) {
        val detail = data.detail
        movieName.text = movie.title
        movieNameTop.text = movie.title
        movieCountry.setText(detail.country)
        movieLanguage.setText(detail.language)
        movieDuration.setText(detail.duration)
        movieReleaseTime.text = detail.releaseTime
        movieDescription.text = detail.description

        movieImage.loadImage(movie.poster)
        movieBigImage.loadImage(movie.poster)

        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        backBtn.setOnClickListener {
            onBack()
        }
        shareBtn.setOnClickListener {
            shareMovie()
        }
    }

    private fun shareMovie() {
        Toast.makeText(requireContext(), "Not Implemented yet!!", Toast.LENGTH_SHORT).show()
    }

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {

    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {}

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        binding.appbar.setBackgroundColor(Color.BLACK)
        if (currentId == R.id.end) {
            disableRefresher()
        } else {
            enableRefresher()
        }
    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}