package com.example.omdb.ui.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.domain.MovieUseCase
import com.example.omdb.utils.STARTING_PAGE_INDEX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelHome @Inject constructor(
    private val useCase: MovieUseCase,
) : ViewModel() {

    val connectionState = useCase.connectionState
    val pagingFlow = useCase.search("Batman", STARTING_PAGE_INDEX).cachedIn(viewModelScope)

}
