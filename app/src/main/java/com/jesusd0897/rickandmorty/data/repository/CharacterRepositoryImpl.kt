package com.jesusd0897.rickandmorty.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jesusd0897.rickandmorty.data.remote.datasource.CharacterPagingSource
import com.jesusd0897.rickandmorty.data.remote.mapper.toEntity
import com.jesusd0897.rickandmorty.data.remote.service.CharacterApiService
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class CharacterRepositoryImpl(
    private val apiService: CharacterApiService
) : CharacterRepository {

    override fun getCharacters(
        pageSize: Int,
        nameQuery: String,
    ): Flow<PagingData<CharacterEntity>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            CharacterPagingSource(apiService = apiService, nameQuery = nameQuery)
        }
    ).flow

    override suspend fun getCharacterById(characterId: Int): Result<CharacterEntity> =
        runCatching {
            withContext(Dispatchers.IO) {
                apiService.getCharacterById(characterId).toEntity()
            }
        }

}