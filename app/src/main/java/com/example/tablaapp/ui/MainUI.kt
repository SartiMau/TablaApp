package com.example.tablaapp.ui

import android.content.Context
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.input.TextFieldValue
import androidx.ui.layout.Column
import androidx.ui.material.AlertDialog
import androidx.ui.material.Button
import androidx.ui.material.IconButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.OutlinedTextField
import androidx.ui.material.TopAppBar
import androidx.ui.res.vectorResource
import com.example.tablaapp.R
import com.example.tablaapp.util.EMPTY_STRING
import com.example.tablaapp.viewmodel.MainViewModel

@Composable
fun setToolbar(context: Context, viewModel: MainViewModel) {
    MaterialTheme {
        TopAppBar(
            title = { Text(context.getString(R.string.app_name)) },
            navigationIcon = {
                IconButton(onClick = { viewModel.onNavigationIconClicked() }) {
                    Icon(vectorResource(R.drawable.ic_baseline_arrow_back_24))
                }
            },
            actions = {
                IconButton(onClick = { viewModel.showDialogAddNewPlayer() }) {
                    Icon(vectorResource(R.drawable.ic_baseline_person_add_24))
                }
            }
        )
    }
}

@Composable
fun showDialogAddNewPlayer(
    context: Context,
    viewModel: MainViewModel
) {
    val textFieldStatus = state { TextFieldValue(EMPTY_STRING) }
    AlertDialog(
        onCloseRequest = { viewModel.closeDialog() },
        title = { Text(context.getString(R.string.main_activity_dialog_title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = textFieldStatus.value,
                    onValueChange = { textFieldStatus.value = it },
                    label = { Text(text = context.getString(R.string.main_activity_dialog_label_text)) }
                )
                if (viewModel.mainState.value.dialogData.showErrorText) {
                    Text(
                        text = context.getString(R.string.main_activity_dialog_empty_value_text_error),
                        color = viewModel.mainState.value.dialogData.labelColor
                    )
                }
            }
        },
        dismissButton = {
            Button(onClick = { viewModel.closeDialog() }) {
                Text(context.getString(R.string.main_activity_dialog_cancel_button))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.onConfirmDialogButton(textFieldStatus.value.text)
                }
            ) { Text(context.getString(R.string.main_activity_dialog_confirmation_button)) }
        }
    )
}