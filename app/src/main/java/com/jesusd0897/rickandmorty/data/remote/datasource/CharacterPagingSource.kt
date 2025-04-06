package com.jesusd0897.rickandmorty.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jesusd0897.rickandmorty.data.remote.mapper.toEntity
import com.jesusd0897.rickandmorty.data.remote.service.CharacterApiService
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity

/**
 * The paging source for the characters.
 * @property apiService The API service.
 * @property nameQuery The name query.
 */
internal class CharacterPagingSource(
    private val apiService: CharacterApiService,
    private val nameQuery: String,
) : PagingSource<Int, CharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> =
        try {
            val currentPage = params.key ?: 1
            val response = apiService.getCharacters(currentPage, nameQuery)
            val characters = response.results.map { it.toEntity() }

            LoadResult.Page(
                data = characters,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.info.next == null) null else currentPage + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }

}