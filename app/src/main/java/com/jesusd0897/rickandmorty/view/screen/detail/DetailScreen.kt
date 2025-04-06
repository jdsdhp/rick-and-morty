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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import com.jesusd0897.rickandmorty.view.navigation.DetailNavDestination as NavDestination
import com.jesusd0897.rickandmorty.view.screen.detail.DetailViewModel as ViewModel

@Composable
internal fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewModel = koinViewModel(),
    onNavigate: (NavDestination) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val character by viewModel.character.collectAsStateWithLifecycle(null)

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = character?.name ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                onNavigate(NavDestination.Back)
            }) {
                Text(text = "Go Back")
            }
        }
    }
}