package com.example.omdb.ui.fragment.detail

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.omdb.R
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.result.Result
import com.example.omdb.databinding.FragmentMovieDetailBinding
import com.example.omdb.databinding.SmallMovieItemBinding
import com.example.omdb.ui.fragment.RefreshableFragment
import com.example.omdb.ui.fragment.adapter.ItemListAdapter
import com.example.omdb.ui.fragment.adapter.diffcallback.MovieDiffCallback
import com.example.omdb.ui.fragment.adapter.factory.SmallMovieItemFactory
import com.example.omdb.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class FragmentMovieDetail : RefreshableFragment(R.layout.fragment_movie_detail),
    MotionLayout.TransitionListener {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding: FragmentMovieDetailBinding get() = _binding!!

    private val viewModel: ViewModelMovieDetail by viewModels()
    private val args: FragmentMovieDetailArgs by navArgs()
    private val movie get() = args.movie
    private val navController by lazy {
        findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupTransitionAnimation()
    }

    private fun setupTransitionAnimation() {
        val context = requireContext()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.shared_image)
        sharedElementReturnTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.shared_image)
    }

    override fun onStart() {
        super.onStart()
        setupStatusBar()
    }

    private fun setupStatusBar() {
        setupFullScreen()
        changeColorOfStatusBar(android.R.color.transparent)
    }

    override fun onStop() {
        super.onStop()
        restoreFullScreen()
    }

    override fun onChildViewCreated(view: View) {
        _binding = FragmentMovieDetailBinding.bind(view)

        load()
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

        launch {
            viewModel.similarStateFlow.collectLatest {
                when (it) {
                    is Result.Fail -> {
                        similarTitle.isVisible = false
                        similarList.isVisible = false
                    }
                    is Result.Success -> {
                        setupSimilarList(it.data())
                        similarTitle.isVisible = true
                        similarList.isVisible = true
                    }
                }
            }
        }

        root.addTransitionListener(this@FragmentMovieDetail)
    }

    private fun setupSimilarList(list: List<Movie>) = with(binding) {
        val itemAdapter = ItemListAdapter(
            diffCallback = MovieDiffCallback(),
            bindableFactory = SmallMovieItemFactory(),
            onItemClick = this@FragmentMovieDetail::onItemClick,
        )
        similarList.adapter = itemAdapter
        itemAdapter.submitList(list)
    }

    private fun onItemClick(view: View, movie: Movie) {
        val imageView = SmallMovieItemBinding.bind(view).image
        val extras = FragmentNavigatorExtras(
            imageView to movie.title
        )
        navController.navigate(
            FragmentMovieDetailDirections.actionFragmentMovieDetailSelf(movie, movie.title),
            navigatorExtras = extras,
        )
    }

    private fun onFail() {
        showError(this@FragmentMovieDetail::refresh)
    }

    override fun refresh() {
        load(true)
    }

    private fun load(isRefreshing: Boolean = false) {
        viewModel.loadData(movie.id, isRefreshing)
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

        movieImage.apply {
            loadImage(movie.poster) {
                startPostponedEnterTransition()
            }
            ViewCompat.setTransitionName(this, args.sharedImageName)
        }
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
    ) {
    }

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
    ) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.similarList.adapter = null
        _binding = null
    }
}