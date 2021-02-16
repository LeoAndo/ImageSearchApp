package com.template.imagesearchapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.template.imagesearchapp.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1
private const val UNSPLASH_LAST_PAGE_INDEX = 5
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
            val photos = api.searchPhotos(query, position, params.loadSize).results
            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty() || position == UNSPLASH_LAST_PAGE_INDEX) null else position + 1 // paging終了するときは、nullをセット.
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            Log.d(TAG, "ex: $ex") // なぜかたまに、403になる時ある.
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UnSplashPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}