package com.jesusd0897.rickandmorty.data.repository

import com.jesusd0897.rickandmorty.data.remote.service.CharacterApiService
import com.jesusd0897.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal class CharacterRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: CharacterApiService
    private lateinit var repository: CharacterRepository

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CharacterApiService::class.java)

        repository = CharacterRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * Verifies that getCharacterById returns a valid CharacterEntity
     * when the API responds with a valid JSON payload.
     */
    @Test
    fun `getCharacterById returns CharacterEntity on success`() = runTest {
        val responseBody = """
            {
              "id": 1,
              "name": "Rick Sanchez",
              "status": "Alive",
              "species": "Human",
              "type": "",
              "gender": "Male",
              "origin": { "name": "Earth", "url": "" },
              "location": { "name": "Earth", "url": "" },
              "image": "",
              "episodes": ["https://episode1"],
              "url": "https://rick.com",
              "created": "2022-01-01T00:00:00Z"
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(responseBody).setResponseCode(200))

        val result = repository.getCharacterById(1)

        assertTrue(result.isSuccess)
        val character = result.getOrNull()!!
        assertEquals(1, character.id)
        assertEquals("Rick Sanchez", character.name)
        assertEquals("Alive", character.status)
        assertEquals("Human", character.species)
        assertEquals("Male", character.gender)
        assertEquals(listOf("https://episode1"), character.episodes)
        assertEquals("https://rick.com", character.url)
        assertEquals("2022-01-01T00:00:00Z", character.created)
    }

    /**
     * Verifies that getCharacterById returns a failure
     * when the API responds with an HTTP error (e.g., 404).
     */
    @Test
    fun `getCharacterById returns failure on HTTP error`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val result = repository.getCharacterById(999)

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    /**
     * Verifies that getCharacterById returns a failure
     * when the API responds with malformed JSON.
     */
    @Test
    fun `getCharacterById returns failure on malformed JSON`() = runTest {
        val malformedBody = """{ "id": 1, "name": "Rick""" // malformed

        mockWebServer.enqueue(MockResponse().setBody(malformedBody).setResponseCode(200))

        val result = repository.getCharacterById(1)

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

}