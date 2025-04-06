package com.jesusd0897.rickandmorty.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jesusd0897.rickandmorty.data.remote.datasource.CharacterPagingSource
import com.jesusd0897.rickandmorty.data.remote.service.CharacterApiService
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

internal class CharacterRepositoryImpl(
    private val apiService: CharacterApiService
) : CharacterRepository {

    override fun getCharacters(pageSize: Int): Flow<PagingData<CharacterEntity>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            CharacterPagingSource(apiService)
        }
    ).flow
}