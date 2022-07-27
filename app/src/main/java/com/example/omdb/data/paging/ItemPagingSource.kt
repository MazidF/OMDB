package com.example.omdb.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.omdb.data.result.Result
import java.lang.Exception

abstract class ItemPagingSource<T : Any> : PagingSource<Int, T>() {

    abstract suspend fun loadData(page: Int, perPage: Int): Result<List<T>>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return try {
            val result = loadData(page, params.loadSize)

            if (result is Result.Success) {
                val data = result.data
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page + 1,
                    nextKey = if (data.isEmpty()) null else page + 1,
                )
            } else {
                LoadResult.Error((result as Result.Fail).error.toThrowable())
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { page ->
                page.nextKey?.minus(1) ?: page.prevKey?.plus(1)
            }
        }
    }
}