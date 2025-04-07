package com.jesusd0897.rickandmorty.data.datasource

import androidx.paging.PagingSource
import com.jesusd0897.rickandmorty.data.remote.datasource.CharacterPagingSource
import com.jesusd0897.rickandmorty.data.remote.service.CharacterApiService
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.entity.LocationEntity
import com.jesusd0897.rickandmorty.domain.entity.OriginEntity
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class CharacterPagingSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: CharacterApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CharacterApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * Verifies that when the API responds with character data,
     * the PagingSource emits a LoadResult.Page with the correct data.
     */
    @Test
    fun `load returns Page when API responds with data`() = runTest {
        val mockResponseBody = """
            {
              "info": {
                "count": 1,
                "pages": 1,
                "next": null,
                "prev": null
              },
              "results": [
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
              ]
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseBody)
        )

        val pagingSource = CharacterPagingSource(apiService = api, nameQuery = "Rick")

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        val expected = listOf(
            CharacterEntity(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                gender = "Male",
                image = "",
                origin = OriginEntity("Earth", ""),
                location = LocationEntity("Earth", ""),
                episodes = listOf("https://episode1"),
                url = "https://rick.com",
                created = "2022-01-01T00:00:00Z",
                type = ""
            )
        )

        assertEquals(
            PagingSource.LoadResult.Page(
                data = expected,
                prevKey = null,
                nextKey = null
            ),
            result
        )
    }

    /**
     * Verifies that when the API returns a 500 error,
     * the PagingSource returns a LoadResult.Error as expected.
     */
    @Test
    fun `load returns Error when API fails`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )

        val pagingSource = CharacterPagingSource(apiService = api, nameQuery = "Rick")

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Error)
    }

    /**
     * Verifies that when the API returns an empty list,
     * the PagingSource emits a LoadResult.Page with no data.
     */
    @Test
    fun `load returns Page with empty list when API returns no results`() = runTest {
        val emptyResponse = """
            {
              "info": {
                "count": 0,
                "pages": 0,
                "next": null,
                "prev": null
              },
              "results": []
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(emptyResponse)
        )

        val pagingSource = CharacterPagingSource(apiService = api, nameQuery = "UnknownCharacter")

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        val expected = emptyList<CharacterEntity>()

        assertEquals(
            PagingSource.LoadResult.Page(
                data = expected,
                prevKey = null,
                nextKey = null
            ),
            result
        )
    }

    /**
     * Verifies that when appending, the PagingSource parses pagination info
     * and emits the expected data and page keys.
     */
    @Test
    fun `load returns Page for append when API responds with next page`() = runTest {
        val mockResponseBody = """
            {
              "info": {
                "count": 2,
                "pages": 2,
                "next": "https://rickandmortyapi.com/api/character/?page=3",
                "prev": "https://rickandmortyapi.com/api/character/?page=1"
              },
              "results": [
                {
                  "id": 2,
                  "name": "Morty Smith",
                  "status": "Alive",
                  "species": "Human",
                  "type": "",
                  "gender": "Male",
                  "origin": { "name": "Earth", "url": "" },
                  "location": { "name": "Earth", "url": "" },
                  "image": "",
                  "episodes": ["https://episode2"],
                  "url": "https://morty.com",
                  "created": "2022-01-02T00:00:00Z"
                }
              ]
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponseBody)
        )

        val pagingSource = CharacterPagingSource(apiService = api, nameQuery = "")

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(
                key = 2,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        val expected = listOf(
            CharacterEntity(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                gender = "Male",
                image = "",
                origin = OriginEntity("Earth", ""),
                location = LocationEntity("Earth", ""),
                episodes = listOf("https://episode2"),
                url = "https://morty.com",
                created = "2022-01-02T00:00:00Z",
                type = ""
            )
        )

        assertEquals(
            PagingSource.LoadResult.Page(
                data = expected,
                prevKey = 1,
                nextKey = 3
            ),
            result
        )
    }
}