package com.jesusd0897.rickandmorty.data.mapper

import com.jesusd0897.rickandmorty.data.remote.dto.CharacterDto
import com.jesusd0897.rickandmorty.data.remote.dto.LocationDto
import com.jesusd0897.rickandmorty.data.remote.dto.OriginDto
import com.jesusd0897.rickandmorty.data.remote.mapper.toEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class CharacterDtoMapperTest {

    /**
     * Verifies that all fields from CharacterDto are correctly mapped to CharacterEntity.
     */
    @Test
    fun `toEntity maps all CharacterDto fields correctly`() {
        val dto = CharacterDto(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            image = "https://rick.com/image.png",
            origin = OriginDto(name = "Earth", url = "https://earth.com"),
            location = LocationDto(name = "Citadel of Ricks", url = "https://citadel.com"),
            episodes = listOf("https://ep1", "https://ep2"),
            url = "https://rick.com",
            created = "2022-01-01T00:00:00Z",
            type = "Humanoid"
        )

        val entity = dto.toEntity()

        assertEquals(dto.id, entity.id)
        assertEquals(dto.name, entity.name)
        assertEquals(dto.status, entity.status)
        assertEquals(dto.species, entity.species)
        assertEquals(dto.gender, entity.gender)
        assertEquals(dto.image, entity.image)
        assertEquals(dto.origin.name, entity.origin.name)
        assertEquals(dto.origin.url, entity.origin.url)
        assertEquals(dto.location.name, entity.location.name)
        assertEquals(dto.location.url, entity.location.url)
        assertEquals(dto.episodes, entity.episodes)
        assertEquals(dto.url, entity.url)
        assertEquals(dto.created, entity.created)
        assertEquals(dto.type, entity.type)
    }

    /**
     * Verifies that toEntity handles unexpected or empty values without throwing exceptions,
     * and maps them safely into default/empty values in CharacterEntity.
     */
    @Test
    fun `toEntity handles empty and unexpected values safely`() {
        val dto = CharacterDto(
            id = -999,
            name = "",
            status = "",
            species = "",
            gender = "Unknown-X",
            image = "",
            origin = OriginDto(name = "", url = ""),
            location = LocationDto(name = "", url = ""),
            episodes = emptyList(),
            url = "",
            created = "",
            type = ""
        )

        val entity = dto.toEntity()

        assertEquals(-999, entity.id)
        assertEquals("", entity.name)
        assertEquals("", entity.status)
        assertEquals("", entity.species)
        assertEquals("Unknown-X", entity.gender)
        assertEquals("", entity.image)
        assertEquals("", entity.origin.name)
        assertEquals("", entity.origin.url)
        assertEquals("", entity.location.name)
        assertEquals("", entity.location.url)
        assertTrue(entity.episodes.isEmpty())
        assertEquals("", entity.url)
        assertEquals("", entity.created)
        assertEquals("", entity.type)
    }

}