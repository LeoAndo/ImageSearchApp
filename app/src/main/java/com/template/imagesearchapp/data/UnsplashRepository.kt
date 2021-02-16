package com.template.imagesearchapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.template.imagesearchapp.api.UnsplashApi
import javax.inject.Inject

class UnsplashRepository @Inject constructor(private val api: UnsplashApi) {
    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                UnsplashPagingSource(
                    api, query
                )
            }
        ).flow
}