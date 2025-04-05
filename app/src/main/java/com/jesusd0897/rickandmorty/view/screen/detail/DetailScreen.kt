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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jesusd0897.rickandmorty.view.navigation.DetailNavDestination

@Composable
internal fun DetailScreen(
    modifier: Modifier = Modifier,
    onNavigate: (DetailNavDestination) -> Unit,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Detail")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                onNavigate(DetailNavDestination.Back)
            }) {
                Text(text = "Go Back")
            }
        }
    }
}