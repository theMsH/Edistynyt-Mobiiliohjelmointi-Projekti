package com.example.edistynytmobiiliohjelmointiprojekti

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun MyAlert(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    confirmButtonText: String,
    dismissButtonText: String
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Icon of alertdialog")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(dismissButtonText)
            }
        }
    )
}


@Composable
fun DeleteDialog(
    showDeleteDialog: MutableState<Boolean>,
    name: String,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = { showDeleteDialog.value = false }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .size(width = 300.dp, height = 300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.scale(2f),
                    tint = Color.Red,
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "Delete $name",
                    modifier = Modifier.padding(10.dp, 26.dp, 10.dp, 26.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextButton(
                        onClick = {
                            showDeleteDialog.value = false
                        }
                    ) {
                        Text(style = MaterialTheme.typography.bodyLarge, text = "Cancel")
                    }
                    TextButton(
                        onClick = {
                            showDeleteDialog.value = false
                            onConfirm()
                        }
                    ) {
                        Text(style = MaterialTheme.typography.bodyLarge, text = "Delete", color = Color.Red)
                    }
                }
            }
        }
    }

}


@Composable
fun CreateNewDialog(
    showCreateNewDialog: MutableState<Boolean>,
    onConfirm: (String) -> Unit,
    title: String = "Title here",
    placeholder: String = "",
    notValidNames: List<String> = emptyList()
) {
    val nameState = rememberSaveable { mutableStateOf(String()) }
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        focusRequester.requestFocus()

        textState.value = textState.value.copy(nameState.value, TextRange(nameState.value.length))
    }

    Dialog(onDismissRequest = { showCreateNewDialog.value = false }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .size(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = title,
                    modifier = Modifier.padding(10.dp),
                )

                // Error text
                if (notValidNames.contains(nameState.value)) {
                    Text(
                        text = "Category already exists!",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else Text(text = "")

                TextField(
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .focusRequester(focusRequester),
                    value = textState.value,
                    placeholder = { Text(text = placeholder) },
                    onValueChange = {
                        nameState.value = it.text
                        Log.d("cursor", "${textState.value.text.length}")
                        textState.value = it.copy(it.text, TextRange(it.text.length))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = (
                            if (nameState.value == ""
                                || notValidNames.contains(nameState.value)) {
                                    ImeAction.Done
                            }
                            else ImeAction.Go
                        )
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            if (nameState.value != "") {
                                defaultKeyboardAction(imeAction = ImeAction.Done)
                                showCreateNewDialog.value = false
                                onConfirm(nameState.value)
                            }
                        }
                    )
                )
                Spacer(modifier = Modifier.height(25.dp))
                Button(
                    enabled = nameState.value != ""
                            && !notValidNames.contains(nameState.value),
                    onClick = {
                        showCreateNewDialog.value = false
                        onConfirm(nameState.value)
                    }
                ) {
                    Text(style = MaterialTheme.typography.bodyLarge, text = "Create")
                }
            }
        }
    }


}
