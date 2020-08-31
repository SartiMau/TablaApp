package com.example.tablaapp.ui

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope.gravity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabConstants.defaultTabIndicatorOffset
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.state
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.annotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import com.example.domain.entity.User
import com.example.domain.entity.WinnerMonth
import com.example.tablaapp.R
import com.example.tablaapp.ui.theme.blackTextColor
import com.example.tablaapp.ui.theme.cardElevation
import com.example.tablaapp.ui.theme.cardRoundedCornerShape
import com.example.tablaapp.ui.theme.fancyIndicatorBoxPaddingBottom
import com.example.tablaapp.ui.theme.fancyIndicatorBoxPreferredHeight
import com.example.tablaapp.ui.theme.initScreenColumnPaddingBottom
import com.example.tablaapp.ui.theme.listItemIconModifierPadding
import com.example.tablaapp.ui.theme.listItemIconModifierSize
import com.example.tablaapp.ui.theme.listItemTrailingFontSize
import com.example.tablaapp.ui.theme.monthTextSize
import com.example.tablaapp.ui.theme.paddingStartDropMenuItemText
import com.example.tablaapp.ui.theme.rowPadding
import com.example.tablaapp.ui.theme.whiteBackgroundColor
import com.example.tablaapp.util.ACTUAL
import com.example.tablaapp.util.ADD_NEW_PLAYER
import com.example.tablaapp.util.EMPTY_STRING
import com.example.tablaapp.util.HISTORY
import com.example.tablaapp.util.ONE_INT
import com.example.tablaapp.util.RESET_POINTS
import com.example.tablaapp.util.ROUNDED_CORNER_SHAPE_PERCENT
import com.example.tablaapp.util.ZERO_INT
import com.example.tablaapp.viewmodel.MainViewModel

@Composable
fun setToolbar(context: Context, viewModel: MainViewModel) {
    MaterialTheme {
        TopAppBar(
            title = { Text(context.getString(R.string.app_name)) },
            navigationIcon = {
                IconButton(onClick = { viewModel.onNavigationIconClicked() }) { Icon(vectorResource(R.drawable.ic_baseline_arrow_back_24)) }
            },
            actions = {
                if (viewModel.mainState.value.tabState == ACTUAL) {
                    DropdownMenu(
                        toggle = {
                            IconButton(onClick = { viewModel.showMoreOptions() }) { Icon(vectorResource(R.drawable.ic_baseline_more_vert)) }
                        },
                        expanded = viewModel.mainState.value.showMoreOptions,
                        onDismissRequest = { viewModel.showMoreOptions() }
                    ) {
                        DropdownMenuItem(onClick = { viewModel.showDialogAddNewPlayer() }) {
                            Row {
                                Icon(vectorResource(R.drawable.ic_baseline_person_add_24))
                                Text(text = ADD_NEW_PLAYER, modifier = Modifier.padding(start = paddingStartDropMenuItemText))
                            }
                        }
                        DropdownMenuItem(onClick = { viewModel.resetPlayerPoints() }) {
                            Row {
                                Icon(vectorResource(R.drawable.ic_baseline_autorenew))
                                Text(text = RESET_POINTS, modifier = Modifier.padding(start = paddingStartDropMenuItemText))
                            }
                        }
                    }
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
        onDismissRequest = { viewModel.closeDialog() },
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
    LazyColumnFor(items = listOfPlayers) {
        Row(modifier = Modifier.fillMaxWidth().padding(rowPadding)) {
            Card(
                backgroundColor = whiteBackgroundColor,
                shape = RoundedCornerShape(cardRoundedCornerShape),
                elevation = cardElevation,
                modifier = Modifier.fillMaxWidth()
            ) {
                val imageModifier = Modifier
                    .padding(listItemIconModifierPadding)
                    .clip(shape = CircleShape)
                    .size(listItemIconModifierSize)
                Box(modifier = Modifier.wrapContentSize().animateContentSize()) {
                    ListItem(
                        icon = { Image(imageResource(R.drawable.photo_1), modifier = imageModifier, contentScale = ContentScale.Crop) },
                        text = { Text(text = it.name, style = MaterialTheme.typography.h5) },
                        trailing = { Text(text = it.points.toString(), style = TextStyle(fontSize = listItemTrailingFontSize)) },
                        modifier = Modifier.clickable(onClick = { viewModel.showCardButtons(it) })
                    )
                    if (viewModel.mainState.value.mainCard.nameOfCardToOpen == it.name) {
                        Row(modifier = Modifier.gravity(Alignment.CenterHorizontally)) {
                            Image(
                                vectorResource(R.drawable.ic_baseline_arrow_drop_up),
                                modifier = Modifier
                                    .clickable(onClick = { viewModel.addPointToPlayer(it) })
                            )
                            if (viewModel.mainState.value.mainCard.enableButton) {
                                Image(
                                    vectorResource(R.drawable.ic_baseline_arrow_drop_down_enable),
                                    Modifier.clickable(onClick = { viewModel.removePointToPlayer(it) })
                                )
                            } else {
                                Image(vectorResource(R.drawable.ic_baseline_arrow_drop_down_disable))
                            }
                            Column(modifier = Modifier.gravity(Alignment.Bottom)) {
                                Icon(
                                    vectorResource(R.drawable.ic_baseline_delete),
                                    modifier = Modifier
                                        .gravity(Alignment.End)
                                        .clickable(onClick = { viewModel.showDialogDeletePlayer(it) })
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun showCurrentMonth(month: String) {
    Text(
        text = annotatedString {
            withStyle(
                SpanStyle(
                    color = blackTextColor,
                    fontSize = monthTextSize,
                    fontWeight = FontWeight.W800,
                    fontStyle = FontStyle.Normal
                )
            ) {
                append(month)
            }
        },
        modifier = Modifier.gravity(Alignment.CenterVertically)
    )
}

@Composable
fun fancyIndicatorTabs(viewModel: MainViewModel) {
    val titles = listOf(ACTUAL, HISTORY)
    val actualTab = if (viewModel.mainState.value.tabState == ACTUAL) ZERO_INT else ONE_INT
    TabRow(
        selectedTabIndex = actualTab,
        indicator = { tabPositions: List<TabPosition> ->
            fancyIndicator(
                Modifier.defaultTabIndicatorOffset(tabPositions[actualTab])
            )
        }
    ) {
        titles.forEachIndexed { index, _ ->
            Tab(
                selected = index == actualTab,
                onClick = { viewModel.showTabSelected(index) },
                text = {
                    Text(
                        text = when (index) {
                            ZERO_INT -> titles[ZERO_INT]
                            ONE_INT -> titles[ONE_INT]
                            else -> EMPTY_STRING
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun fancyIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    Spacer(
        modifier.fillMaxWidth()
            .padding(bottom = fancyIndicatorBoxPaddingBottom)
            .preferredHeight(fancyIndicatorBoxPreferredHeight)
            .background(
                color,
                RoundedCornerShape(
                    bottomLeftPercent = ROUNDED_CORNER_SHAPE_PERCENT,
                    bottomRightPercent = ROUNDED_CORNER_SHAPE_PERCENT
                )
            )
    )
}

@Composable
fun showHistoryScreenContent(listOfWinners: ArrayList<WinnerMonth>, viewModel: MainViewModel) {
    LazyColumnFor(items = listOfWinners) {
        Row(modifier = Modifier.fillMaxWidth().padding(rowPadding)) {
            Card(
                backgroundColor = whiteBackgroundColor,
                shape = RoundedCornerShape(cardRoundedCornerShape),
                elevation = cardElevation,
                modifier = Modifier.fillMaxWidth()
            ) {
                val imageModifier = Modifier
                    .padding(listItemIconModifierPadding)
                    .clip(shape = RectangleShape)
                    .size(listItemIconModifierSize)
                Box {
                    ListItem(
                        text = { Text(text = it.winnerMonth, style = MaterialTheme.typography.h5) },
                        icon = { Icon(vectorResource(id = R.drawable.ic_baseline_calendar)) },
                        modifier = Modifier.clickable(onClick = { viewModel.showMonthCard(it.winnerMonth) })
                    )
                    Column(modifier = Modifier.gravity(Alignment.CenterVertically)) {
                        if (it.winnerMonth == viewModel.mainState.value.openMonthCard) {
                            ListItem(
                                icon = { Image(imageResource(R.drawable.crown), modifier = imageModifier) },
                                text = { Text(it.winnerName, style = MaterialTheme.typography.h5) },
                                trailing = { Text(it.winnerPoint.toString(), style = MaterialTheme.typography.h5) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun initScreen(context: Context, viewModel: MainViewModel) {
    Scaffold(
        topBar = { setToolbar(context, viewModel) },
        bottomBar = { fancyIndicatorTabs(viewModel) },
        bodyContent = {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = initScreenColumnPaddingBottom)) {
                if (viewModel.mainState.value.tabState == ACTUAL) {
                    showCurrentMonth(viewModel.mainState.value.currentMonth)
                    showMainScreenContent(viewModel.mainState.value.listOfPlayers, viewModel)
                } else {
                    showHistoryScreenContent(viewModel.mainState.value.listOfWinners, viewModel)
                }
            }
        }
    )
}

@Composable
fun showDialogAddNewPlayerScreen(context: Context, viewModel: MainViewModel) {
    Scaffold(
        topBar = { setToolbar(context, viewModel) },
        bottomBar = { fancyIndicatorTabs(viewModel) },
        bodyContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                showCurrentMonth(viewModel.mainState.value.currentMonth)
                showDialogAddNewPlayer(context, viewModel)
                showMainScreenContent(viewModel.mainState.value.listOfPlayers, viewModel)
            }
        }
    )
}

@Composable
fun showDialogDeletePlayer(context: Context, user: User, viewModel: MainViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.closeDeletePlayerDialog(user) },
        title = { Text(context.getString(R.string.main_activity_delete_dialog_title)) },
        text = {
            Text(text = context.getString(R.string.main_activity_delete_dialog_text, user.name))
        },
        dismissButton = {
            Button(onClick = { viewModel.closeDeletePlayerDialog(user) }) {
                Text(context.getString(R.string.main_activity_dialog_cancel_button))
            }
        },
        confirmButton = {
            Button(onClick = { viewModel.onConfirmDeleteDialogButton(user) }) {
                Text(context.getString(R.string.main_activity_dialog_confirmation_button))
            }
        }
    )
}

@Composable
fun showDialogDeletePlayerScreen(context: Context, user: User, viewModel: MainViewModel) {
    Scaffold(
        topBar = { setToolbar(context, viewModel) },
        bottomBar = { fancyIndicatorTabs(viewModel) },
        bodyContent = {
            Column {
                showCurrentMonth(viewModel.mainState.value.currentMonth)
                showDialogDeletePlayer(context, user, viewModel)
                showMainScreenContent(viewModel.mainState.value.listOfPlayers, viewModel)
            }
        }
    )
}
