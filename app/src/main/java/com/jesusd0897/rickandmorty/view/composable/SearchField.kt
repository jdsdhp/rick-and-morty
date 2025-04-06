package com.jesusd0897.rickandmorty.view.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction

/**
 * A composable function that provides a search field with optional leading and trailing icons.
 * @param modifier The modifier to be applied to the search field.
 * @param value The current text value of the search field.
 * @param label The label to be displayed for the search field.
 * @param trailingIconContentDescription The content description for the trailing icon.
 * @param singleLine Whether the search field should be single-line or multi-line. Defaults to true.
 * @param focusRequester A [FocusRequester] to control the focus of the search field.
 * @param keyboardOptions The keyboard options to be applied to the search field. Defaults to [KeyboardOptions] with [ImeAction.Search].
 * @param keyboardActions The keyboard actions to be performed when the user interacts with the keyboard. Defaults to [KeyboardActions.Default].
 * @param onValueChange A callback that is invoked when the text value of the search field changes.
 * @param leadingIcon A composable function that provides the leading icon for the search field. Defaults to a search icon.
 * @param trailingIconClick A callback that is invoked when the trailing icon is clicked.
 * @param trailingIcon A composable function that provides the trailing icon for the search field. Defaults to a close icon that appears when the search field has text.
 */
@Composable
internal fun SearchField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    trailingIconContentDescription: String? = null,
    singleLine: Boolean = true,
    focusRequester: FocusRequester? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.TwoTone.Search,
            contentDescription = label,
        )
    },
    trailingIconClick: (() -> Unit)? = null,
    trailingIcon: @Composable () -> Unit = {
        if (value.isNotEmpty()) {
            IconButton(onClick = {
                trailingIconClick?.invoke()
            }) {
                Icon(
                    imageVector = Icons.TwoTone.Close,
                    contentDescription = trailingIconContentDescription,
                )
            }
        }
    },
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .apply { focusRequester?.let { focusRequester(it) } },
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = colors,
    )
}