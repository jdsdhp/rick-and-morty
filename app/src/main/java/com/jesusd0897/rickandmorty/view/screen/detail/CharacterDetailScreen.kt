package com.jesusd0897.rickandmorty.view.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.jesusd0897.rickandmorty.view.theme.Padding
import org.koin.compose.viewmodel.koinViewModel
import com.jesusd0897.rickandmorty.view.navigation.DetailNavDestination as NavDestination
import com.jesusd0897.rickandmorty.view.screen.detail.CharacterDetailViewModel as ViewModel

@Composable
internal fun DetailScreen(
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

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = uiState.character?.name ?: "")
            Spacer(modifier = Modifier.height(Padding.normal))
            Button(onClick = {
                viewModel.onEvent(ViewModel.Event.OnBackClick)
            }) {
                Text(text = "Go Back")
            }
        }
    }
}