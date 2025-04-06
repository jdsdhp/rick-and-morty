package com.jesusd0897.rickandmorty.view.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Female
import androidx.compose.material.icons.twotone.Flag
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material.icons.twotone.Male
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.PlayCircle
import androidx.compose.material.icons.twotone.Science
import androidx.compose.material.icons.twotone.Transgender
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.jesusd0897.rickandmorty.R
import com.jesusd0897.rickandmorty.view.theme.Padding
import org.koin.compose.viewmodel.koinViewModel
import com.jesusd0897.rickandmorty.view.navigation.DetailNavDestination as NavDestination
import com.jesusd0897.rickandmorty.view.screen.detail.CharacterDetailViewModel as ViewModel

@Composable
internal fun CharacterDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewModel = koinViewModel(),
    onNavigate: (NavDestination) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.navEvent.collect { onNavigate(it) }
        }
    }

    ScreenContent(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    uiState: ViewModel.UiState,
    onEvent: (ViewModel.Event) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        uiState.character?.name ?: stringResource(R.string.character_detail),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ViewModel.Event.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.error_loading_character),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(Padding.normal))
                        Button(onClick = { onEvent(ViewModel.Event.OnBackClick) }) {
                            Text(stringResource(R.string.go_back))
                        }
                    }
                }
            }

            uiState.character != null -> {
                val character = uiState.character

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        val backgroundColor = when (character.status.lowercase()) {
                            "alive" -> MaterialTheme.colorScheme.primaryContainer
                            "dead" -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceContainer
                        }

                        OutlinedCard(
                            modifier = Modifier
                                .padding(Padding.normal)
                                .fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.outlinedCardColors(containerColor = backgroundColor),
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(Padding.large),
                                verticalArrangement = Arrangement.spacedBy(Padding.small)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(character.image)
                                        .crossfade(enable = true)
                                        .build(),
                                    contentDescription = character.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clip(MaterialTheme.shapes.medium),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.height(Padding.medium))

                                Text(
                                    text = character.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                )

                                Spacer(modifier = Modifier.height(Padding.small))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.TwoTone.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(Padding.small))
                                    Text(text = "${character.status} - ${character.species}")
                                }

                                if (character.type.isNotBlank()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            modifier = Modifier.size(16.dp),
                                            imageVector = Icons.TwoTone.Science,
                                            contentDescription = null,
                                        )
                                        Spacer(modifier = Modifier.width(Padding.small))
                                        Text(text = stringResource(R.string.type, character.type))
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        imageVector = when (character.gender.lowercase()) {
                                            "female" -> Icons.TwoTone.Female
                                            "male" -> Icons.TwoTone.Male
                                            else -> Icons.TwoTone.Transgender
                                        },
                                        contentDescription = null,
                                    )
                                    Spacer(modifier = Modifier.width(Padding.small))
                                    Text(text = character.gender)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.TwoTone.Flag,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(Padding.small))
                                    Text(
                                        text = stringResource(
                                            R.string.origin,
                                            character.origin.name
                                        )
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.TwoTone.LocationOn,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(Padding.small))
                                    Text(
                                        text = stringResource(
                                            R.string.location,
                                            character.location.name
                                        )
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.TwoTone.PlayCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(Padding.small))
                                    Text(
                                        text = stringResource(
                                            R.string.episodes,
                                            character.episode.size
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}