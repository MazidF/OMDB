package com.example.omdb.ui.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.domain.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewModelHome @Inject constructor(
    private val useCase: MovieUseCase,
) : ViewModel() {

    fun search(title: String): Flow<PagingData<Movie>> {
        return useCase.search(title)
            .cachedIn(viewModelScope)
    }
}
