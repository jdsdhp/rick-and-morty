package com.jesusd0897.rickandmorty.domain.usecase

import com.jesusd0897.rickandmorty.data.DUMMY_CHARACTER_DATA
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity

class GetCharactersUseCase {

    operator fun invoke(): List<CharacterEntity> = DUMMY_CHARACTER_DATA

}