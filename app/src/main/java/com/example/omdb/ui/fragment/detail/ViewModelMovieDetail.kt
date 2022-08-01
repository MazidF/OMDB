package com.example.omdb.ui.fragment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.result.Result
import com.example.omdb.data.result.Result.Companion.loading
import com.example.omdb.domain.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMovieDetail @Inject constructor(
    private val useCase: MovieUseCase,
) : ViewModel() {

    private val _movieStateFlow = MutableStateFlow<Result<MovieDetailWithGenres>>(loading())
    val movieStateFlow get() = _movieStateFlow.asStateFlow()

    private val _similarStateFlow = MutableStateFlow<Result<List<Movie>>>(loading())
    val similarStateFlow get() = _similarStateFlow.asStateFlow()

    fun loadData(movieId: String, isRefreshing: Boolean) {
        if (isRefreshing or (_movieStateFlow.value is Result.Loading<*>)) {
            getDetail(movieId)
            getSimilar(movieId)
        }
    }

    private fun getDetail(movieId: String, isRefreshing: Boolean = false) {
        viewModelScope.launch {
            useCase.getDetail(movieId).collect {
                _movieStateFlow.emit(it)
            }
        }
    }

    private fun getSimilar(movieId: String, isRefreshing: Boolean = false) {
        viewModelScope.launch {
            useCase.getSimilar(movieId).collect {
                _similarStateFlow.emit(it)
            }
        }
    }
}
