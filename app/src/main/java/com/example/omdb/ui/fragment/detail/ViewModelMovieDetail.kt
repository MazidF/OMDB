package com.example.omdb.ui.fragment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.result.Result
import com.example.omdb.data.result.Result.Companion.loading
import com.example.omdb.domain.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMovieDetail @Inject constructor(
    private val useCase: MovieUseCase,
) : ViewModel() {

    private val _movieStateFlow = MutableStateFlow<Result<MovieDetailWithGenres>>(loading())
    val movieStateFlow get() = _movieStateFlow.asStateFlow()

    fun getDetail(movieId: String) {
        viewModelScope.launch {
            useCase.getDetail(movieId).collect {
                _movieStateFlow.emit(it)
            }
        }
    }
}
