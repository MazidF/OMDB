package com.example.omdb.data.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.omdb.data.result.Result
import java.lang.Exception

abstract class ItemPagingSource<T : Any>(
    private val startingPageIndex: Int,
) : PagingSource<Int, T>() {

    abstract suspend fun loadData(page: Int, perPage: Int): Result<List<T>>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val key = params.key ?: startingPageIndex
        return try {
            val result = loadData(key, params.loadSize)

            if (result is Result.Success) {
                val data = result.data()
                LoadResult.Page(
                    data = data,
                    prevKey = if (key == startingPageIndex) null else key.minus(1),
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

    companion object {
        fun <T : Any> pager(config: PagingConfig, startingPageIndex: Int, block: suspend (Int, Int) -> Result<List<T>>): Pager<Int, T> {
            return Pager(config) {
                object : ItemPagingSource<T>(startingPageIndex) {
                    override suspend fun loadData(page: Int, perPage: Int): Result<List<T>> {
                        return block(page, perPage)
                    }
                }
            }
        }
    }
}