package com.example.tablaapp.ui

import android.content.Context
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.foundation.Box
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.input.TextFieldValue
import androidx.ui.input.VisualTransformation
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.size
import androidx.ui.material.AlertDialog
import androidx.ui.material.Button
import androidx.ui.material.Card
import androidx.ui.material.IconButton
import androidx.ui.material.ListItem
import androidx.ui.material.MaterialTheme
import androidx.ui.material.OutlinedTextField
import androidx.ui.material.TopAppBar
import androidx.ui.res.imageResource
import androidx.ui.res.vectorResource
import androidx.ui.text.TextStyle
import com.example.domain.entity.User
import com.example.tablaapp.R
import com.example.tablaapp.ui.theme.cardElevation
import com.example.tablaapp.ui.theme.cardRoundedCornerShape
import com.example.tablaapp.ui.theme.listItemIconModifierPadding
import com.example.tablaapp.ui.theme.listItemIconModifierSize
import com.example.tablaapp.ui.theme.listItemTrailingFontSize
import com.example.tablaapp.ui.theme.rowPadding
import com.example.tablaapp.util.EMPTY_STRING
import com.example.tablaapp.viewmodel.MainViewModel
import java.util.Locale

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
                    label = { Text(text = context.getString(R.string.main_activity_dialog_label_text)) },
                    trailingIcon = { Icon(vectorResource(R.drawable.ic_baseline_person)) }
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

@Composable
fun showMainScreenContent(listOfPlayers: ArrayList<User>, viewModel: MainViewModel) {
    LazyColumnItems(items = listOfPlayers) {
        Row(modifier = Modifier.fillMaxWidth().padding(rowPadding)) {
            Card(
                color = Color.White,
                shape = RoundedCornerShape(cardRoundedCornerShape),
                elevation = cardElevation,
                modifier = Modifier.fillMaxWidth()
            ) {
                val imageModifier = Modifier
                    .padding(listItemIconModifierPadding)
                    .clip(shape = CircleShape)
                    .size(listItemIconModifierSize)
                Box {
                    ListItem(
                        icon = { Image(imageResource(R.drawable.photo_1), modifier = imageModifier, contentScale = ContentScale.Crop) },
                        text = { Text(text = it.name, style = MaterialTheme.typography.h5) },
                        trailing = { Text(text = it.points.toString(), style = TextStyle(fontSize = listItemTrailingFontSize)) },
                        onClick = { viewModel.showCardButtons(it) }
                    )
                    Column(modifier = Modifier.gravity(Alignment.CenterVertically)) {
                        if (viewModel.mainState.value.mainCard.nameOfCardToOpen == it.name) {
                            Row {
                                Clickable(onClick = { viewModel.addPointToPlayer(it) }) {
                                    Image(vectorResource(R.drawable.ic_baseline_arrow_drop_up))
                                }
                                Clickable(
                                    onClick = { viewModel.removePointToPlayer(it) },
                                    enabled = viewModel.mainState.value.mainCard.enableButton
                                ) {
                                    if (viewModel.mainState.value.mainCard.enableButton) {
                                        Image(vectorResource(R.drawable.ic_baseline_arrow_drop_down_enable))
                                    } else {
                                        Image(vectorResource(R.drawable.ic_baseline_arrow_drop_down_disable))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


