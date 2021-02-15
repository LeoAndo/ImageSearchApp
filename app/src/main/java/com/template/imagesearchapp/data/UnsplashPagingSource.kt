package com.template.imagesearchapp.data

import android.util.Log
import androidx.paging.PagingSource
import com.template.imagesearchapp.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1
private val TAG = UnsplashPagingSource::class.java.simpleName

class UnsplashPagingSource(
    private val api: UnsplashApi,
    private val query: String
) : PagingSource<Int, UnSplashPhoto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnSplashPhoto> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        Log.d(TAG, "current thread: " + Thread.currentThread())
        Log.d(
            TAG,
            "query: $query postion: $position loadSize: ${params.loadSize}"
        )

        return try {
            val response = api.searchPhotos(query, position, params.loadSize)
            val photos = response.results
            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }
    }
}