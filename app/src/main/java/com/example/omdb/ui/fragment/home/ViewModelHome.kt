package com.example.omdb.ui.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.omdb.domain.MovieUseCase
import com.example.omdb.utils.STARTING_PAGE_INDEX
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelHome @Inject constructor(
    private val useCase: MovieUseCase,
) : ViewModel() {

    val connectionState = useCase.connectionState
    val pagingFlow = useCase.search("Batman", STARTING_PAGE_INDEX).cachedIn(viewModelScope)

    fun resetConnectionState() {
        useCase.resetConnectionState()
    }
}
