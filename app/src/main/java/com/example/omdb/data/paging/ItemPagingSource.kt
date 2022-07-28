package com.example.omdb.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.omdb.data.result.Result
import com.example.omdb.utils.STARTING_PAGE_INDEX
import java.lang.Exception

abstract class ItemPagingSource<T : Any> : PagingSource<Int, T>() {

    abstract suspend fun loadData(page: Int, perPage: Int): Result<List<T>>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val key = params.key ?: STARTING_PAGE_INDEX
        return try {
            val result = loadData(key, params.loadSize)

            if (result is Result.Success) {
                val data = result.data()
                LoadResult.Page(
                    data = data,
                    prevKey = if (key == STARTING_PAGE_INDEX) null else key.minus(1),
                    nextKey = if (data.isEmpty()) null else key.plus(1),
                )
            } else {
                LoadResult.Error((result as Result.Fail).error().toThrowable())
            }

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.run {
                prevKey?.plus(1) ?: nextKey?.minus(1)
            }
        }
    }
}