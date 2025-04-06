package com.jesusd0897.rickandmorty.view.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.jesusd0897.rickandmorty.view.theme.Padding

/**
 * A composable component that provides a scaffold with an animated search toolbar.
 * This component allows for a title and a search field that can expand or collapse with smooth animations.
 * @param modifier Modifier to be applied to the Scaffold.
 * @param title The title text to display in the toolbar when the search field is not expanded.
 * @param value The current text value of the search field.
 * @param label The label text for the search field, which also serves as the content description for the leading icon.
 * @param trailingIconContentDescription Content description for the trailing icon (e.g., close icon) in the search field.
 * @param singleLine Boolean to determine if the search field should be single line only. Defaults to `true`.
 * @param focusRequester An optional [FocusRequester] to control the focus behavior of the search field.
 * @param keyboardOptions Keyboard options to control IME actions and keyboard configuration. Defaults to `ImeAction.Search`.
 * @param keyboardActions Actions to be performed when IME actions are triggered. Defaults to `KeyboardActions.Default`.
 * @param onValueChange Callback invoked when the value of the search field changes.
 * @param leadingIcon A composable function that provides the leading icon of the search field. Defaults to a search icon.
 * @param trailingIconClick Optional callback invoked when the trailing icon in the search field is clicked.
 *                          This is commonly used for actions like clearing the search field.
 * @param navigationIcon Composable to display a navigation icon at the start of the top app bar. Default is an empty lambda.
 * @param snackbarHost Composable to display a SnackbarHost. Default is an empty lambda.
 * @param content The main content of the scaffold, placed below the top app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchToolbarScaffold(
    modifier: Modifier = Modifier,
    title: String = "",
    value: String,
    label: String,
    trailingIconContentDescription: String? = null,
    singleLine: Boolean = true,
    focusRequester: FocusRequester? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.TwoTone.Search,
            contentDescription = label,
        )
    },
    trailingIconClick: (() -> Unit)? = null,
    navigationIcon: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    var isSearchExpanded: Boolean by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = navigationIcon,
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        AnimatedVisibility(
                            modifier = Modifier.align(Alignment.Center),
                            visible = !isSearchExpanded,
                            enter = fadeIn() + slideInHorizontally(),
                            exit = fadeOut() + slideOutHorizontally(),
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        AnimatedVisibility(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterEnd),
                            visible = isSearchExpanded,
                            enter = fadeIn() + slideInHorizontally(
                                initialOffsetX = { it }
                            ),
                            exit = fadeOut() + slideOutHorizontally(
                                targetOffsetX = { it }
                            ),
                        ) {
                            SearchField(
                                modifier = Modifier
                                    .padding(end = Padding.large)
                                    .align(Alignment.CenterEnd),
                                focusRequester = focusRequester,
                                value = value,
                                onValueChange = onValueChange,
                                label = label,
                                trailingIcon = {
                                    IconButton(onClick = {
                                        trailingIconClick?.invoke()
                                        if (value.isEmpty()) {
                                            isSearchExpanded = false
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.TwoTone.Close,
                                            contentDescription = trailingIconContentDescription,
                                        )
                                    }
                                },
                                trailingIconContentDescription = trailingIconContentDescription,
                                singleLine = singleLine,
                                keyboardActions = keyboardActions,
                                keyboardOptions = keyboardOptions,
                                leadingIcon = leadingIcon,
                            )
                        }
                    }
                },
                actions = {
                    if (!isSearchExpanded) {
                        IconButton(onClick = { isSearchExpanded = true }) {
                            Icon(imageVector = Icons.TwoTone.Search, contentDescription = label)
                        }
                    }
                }
            )
        },
        content = content,
        snackbarHost = snackbarHost,
    )
}