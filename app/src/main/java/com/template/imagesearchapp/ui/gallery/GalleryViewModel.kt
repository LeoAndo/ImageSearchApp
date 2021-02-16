package com.template.imagesearchapp.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.template.imagesearchapp.data.UnsplashRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_QUERY = "current_query"
        private const val DEFAULT_QUERY = "cats"
    }

    init {
        if (!savedStateHandle.contains(KEY_QUERY)) {
            savedStateHandle.set(KEY_QUERY, DEFAULT_QUERY)
        }
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val photos = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty() },
        savedStateHandle.getLiveData(KEY_QUERY, DEFAULT_QUERY).asFlow().flatMapLatest { query ->
            repository.getSearchResults(query)
        }.cachedIn(viewModelScope)
    ).flattenMerge(2)

    fun searchPhotos(query: String) {
        if (query.isNotEmpty() &&
            savedStateHandle.get<String>(KEY_QUERY) != query
        ) {
            savedStateHandle.set(KEY_QUERY, query)
        }
    }

    fun clearList() {
        clearListCh.offer(Unit)
    }

}