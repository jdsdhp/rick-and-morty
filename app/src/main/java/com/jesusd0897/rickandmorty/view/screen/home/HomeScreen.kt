package com.jesusd0897.rickandmorty.view.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.jesusd0897.rickandmorty.R
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.view.theme.Padding
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import com.jesusd0897.rickandmorty.view.navigation.HomeNavDestination as NavDestination
import com.jesusd0897.rickandmorty.view.screen.home.HomeViewModel as ViewModel

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewModel = koinViewModel(),
    onNavigate: (NavDestination) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val characters = viewModel.characters.collectAsLazyPagingItems()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.navEvent.collect { onNavigate(it) }
        }
    }

    ScreenContent(
        modifier = modifier,
        uiState = uiState,
        characters = characters,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    uiState: ViewModel.UiState,
    characters: LazyPagingItems<CharacterEntity>,
    onEvent: (ViewModel.Event) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {

                        },
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Column(Modifier.padding(innerPadding)) {
            when {
                // Empty state
                characters.loadState.refresh is LoadState.NotLoading && characters.itemCount == 0 -> {
                    Box(
                        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.there_are_still_no_characters))
                    }
                }

                else -> {
                    CharactersList(
                        characters = characters,
                        onEvent = onEvent
                    )

                    if (characters.loadState.hasError) {
                        showError = true
                    }
                }
            }
        }

        val errorMessage =
            stringResource(R.string.an_error_has_occurred_please_try_again)
        val retryText = stringResource(R.string.retry)

        LaunchedEffect(showError) {
            if (showError) {
                coroutineScope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = errorMessage,
                        actionLabel = retryText,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        showError = false
                        onEvent(ViewModel.Event.OnRefresh)
                    }
                }
            }
        }
    }
}

@Composable
private fun CharactersList(
    modifier: Modifier = Modifier,
    characters: LazyPagingItems<CharacterEntity>,
    onEvent: (ViewModel.Event) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            if (characters.loadState.refresh is LoadState.Loading && characters.itemCount == 0) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }

        items(characters.itemCount) { index ->
            val item: CharacterEntity? = characters[index]
            item?.let {
                CharacterItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Padding.small)
                        .padding(horizontal = Padding.normal),
                    character = it,
                ) {
                    onEvent(ViewModel.Event.OnItemClick(it))
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(Padding.small))
        }

        item {
            if (characters.loadState.append is LoadState.Loading || characters.loadState.refresh is LoadState.Loading && characters.itemCount > 0) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun CharacterItem(
    modifier: Modifier = Modifier,
    character: CharacterEntity,
    onItemClick: () -> Unit,
) {
    Card(modifier = modifier, onClick = onItemClick) {
        Row {
            AsyncImage(
                modifier = Modifier.size(86.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.image)
                    .crossfade(enable = true)
                    .build(),
                contentDescription = "avatar",
            )
            Column(
                modifier = Modifier.padding(Padding.normal),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(Padding.normal))

                Text(
                    text = "${character.status} - ${character.species}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(text = character.gender, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}