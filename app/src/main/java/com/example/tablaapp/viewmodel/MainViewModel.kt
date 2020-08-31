package com.example.tablaapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.domain.entity.User
import com.example.domain.entity.WinnerMonth
import com.example.domain.util.ZERO_POINT
import com.example.tablaapp.ui.theme.redWarningErrorColor
import com.example.tablaapp.ui.theme.whiteBackgroundColor
import com.example.tablaapp.util.ACTUAL
import com.example.tablaapp.util.EMPTY_STRING
import com.example.tablaapp.util.HISTORY
import com.example.tablaapp.util.MOCK_MONTH_AUGUST
import com.example.tablaapp.util.MOCK_MONTH_SEPTEMBER
import com.example.tablaapp.util.MOCK_NAME_LUCHO
import com.example.tablaapp.util.MOCK_NAME_NICO
import com.example.tablaapp.util.MOCK_POINT_LUCHO
import com.example.tablaapp.util.MOCK_POINT_NICO
import com.example.tablaapp.util.MONTH_SIMPLE_DATE_FORMAT
import com.example.tablaapp.util.ONE_INT
import com.example.tablaapp.util.ZERO_INT
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.FINISH
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.INIT
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.SHOW_DELETE_PLAYER_DIALOG
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.SHOW_DIALOG
import com.example.tablaapp.viewmodel.contract.MainContract
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainViewModel @ViewModelInject constructor() : ViewModel(), MainContract.ViewModel {

    private var mutableMainState: MutableState<MainData> = mutableStateOf(
        MainData(
            INIT,
            MainCardPlayerData(),
            MainDialogData(),
            arrayListOf(),
            SimpleDateFormat(MONTH_SIMPLE_DATE_FORMAT, Locale.ENGLISH).format(Calendar.getInstance().time)
        )
    )

    val mainState: MutableState<MainData>
        get() = mutableMainState

    override fun showDialogAddNewPlayer() {
        mutableMainState.value = MainData(
            SHOW_DIALOG,
            MainCardPlayerData(),
            MainDialogData(mainState.value.dialogData.isEmptyDialogInputText),
            mainState.value.listOfPlayers,
            mainState.value.currentMonth
        )
    }

    override fun onNavigationIconClicked() {
        mutableMainState.value = MainData(
            FINISH,
            MainCardPlayerData(mainState.value.mainCard.nameOfCardToOpen),
            MainDialogData(),
            mainState.value.listOfPlayers,
            mainState.value.currentMonth
        )
    }

    override fun closeDialog() {
        mutableMainState.value = MainData(
            INIT,
            MainCardPlayerData(),
            MainDialogData(),
            mainState.value.listOfPlayers,
            mainState.value.currentMonth
        )
    }

    override fun onConfirmDialogButton(textValue: String) {
        val newList = mutableMainState.value.listOfPlayers
        if (textValue.isEmpty()) {
            mutableMainState.value = MainData(
                SHOW_DIALOG,
                MainCardPlayerData(),
                MainDialogData(isEmptyDialogInputText = true, showErrorText = true, labelColor = redWarningErrorColor),
                mainState.value.listOfPlayers,
                mainState.value.currentMonth
            )
        } else {
            newList.add(User(textValue, ZERO_POINT))
            mutableMainState.value = MainData(
                INIT,
                MainCardPlayerData(),
                MainDialogData(),
                newList,
                mainState.value.currentMonth
            )
        }
    }

    override fun showCardButtons(player: User) {
        if (mainState.value.mainCard.nameOfCardToOpen == player.name) {
            mutableMainState.value = MainData(
                INIT,
                MainCardPlayerData(),
                MainDialogData(),
                mainState.value.listOfPlayers,
                mainState.value.currentMonth
            )
        } else {
            mutableMainState.value = MainData(
                INIT,
                MainCardPlayerData(player.name, player.points != ZERO_POINT),
                MainDialogData(),
                mainState.value.listOfPlayers,
                mainState.value.currentMonth
            )
        }
    }

    override fun addPointToPlayer(player: User) {
        val position = mainState.value.listOfPlayers.indexOf(player)
        mainState.value.listOfPlayers[position].points = mainState.value.listOfPlayers[position].points + ONE_INT
        mutableMainState.value = MainData(
            INIT,
            MainCardPlayerData(),
            MainDialogData(),
            mainState.value.listOfPlayers,
            mainState.value.currentMonth
        )
    }

    override fun removePointToPlayer(player: User) {
        val position = mainState.value.listOfPlayers.indexOf(player)
        mainState.value.listOfPlayers[position].points = mainState.value.listOfPlayers[position].points - ONE_INT
        mutableMainState.value = MainData(
            INIT,
            MainCardPlayerData(
                enableButton = mainState.value.listOfPlayers[position].points != ZERO_POINT
            ),
            MainDialogData(),
            mainState.value.listOfPlayers,
            mainState.value.currentMonth
        )
    }

    override fun showMoreOptions() {
        mutableMainState.value = MainData(
            INIT,
            MainCardPlayerData(),
            MainDialogData(),
            mainState.value.listOfPlayers,
            mainState.value.currentMonth,
            !mainState.value.showMoreOptions
        )
    }

    override fun resetPlayerPoints() {
        mainState.value.listOfPlayers.forEach {
            it.points = ZERO_INT
        }
        mutableMainState.value = MainData(
            INIT,
            MainCardPlayerData(mainState.value.mainCard.nameOfCardToOpen),
            MainDialogData(),
            mainState.value.listOfPlayers,
            mainState.value.currentMonth
        )
    }

    override fun showTabSelected(tabSelected: Int) {
        val year = Calendar.getInstance().get(Calendar.YEAR).toString()
        if (tabSelected == ZERO_INT) {
            mutableMainState.value = MainData(
                INIT,
                MainCardPlayerData(),
                MainDialogData(),
                mainState.value.listOfPlayers,
                mainState.value.currentMonth,
                tabState = ACTUAL
            )
        } else {
            mutableMainState.value = MainData(
                INIT,
                MainCardPlayerData(),
                MainDialogData(),
                mainState.value.listOfPlayers,
                mainState.value.currentMonth,
                tabState = HISTORY,
                listOfWinners = arrayListOf(
                    WinnerMonth("$MOCK_MONTH_AUGUST $year", MOCK_NAME_LUCHO, MOCK_POINT_LUCHO),
                    WinnerMonth("$MOCK_MONTH_SEPTEMBER $year", MOCK_NAME_NICO, MOCK_POINT_NICO)
                )
            )
        }
    }

    override fun showMonthCard(month: String) {
        val monthOpen = if (month == mainState.value.openMonthCard) EMPTY_STRING else month
        val year = Calendar.getInstance().get(Calendar.YEAR).toString()
        mutableMainState.value = MainData(
            INIT,
            MainCardPlayerData(),
            MainDialogData(),
            mainState.value.listOfPlayers,
            mainState.value.currentMonth,
            tabState = HISTORY,
            listOfWinners = arrayListOf(
                WinnerMonth("$MOCK_MONTH_AUGUST $year", MOCK_NAME_LUCHO, MOCK_POINT_LUCHO),
                WinnerMonth("$MOCK_MONTH_SEPTEMBER $year", MOCK_NAME_NICO, MOCK_POINT_NICO)
            ),
            openMonthCard = monthOpen
        )
    }

    override fun showDialogDeletePlayer(user: User) {
        mutableMainState.value = MainData(
            SHOW_DELETE_PLAYER_DIALOG,
            mainCard = MainCardPlayerData(user.name, user.points != ZERO_POINT),
            dialogData = MainDialogData(),
            listOfPlayers = mainState.value.listOfPlayers,
            currentMonth = mainState.value.currentMonth,
            listOfWinners = mainState.value.listOfWinners,
            deleteDialogName = user
        )
    }

    override fun onConfirmDeleteDialogButton(user: User) {
        mainState.value.listOfPlayers.remove(user)
        mutableMainState.value = MainData(
            INIT,
            mainCard = MainCardPlayerData(),
            dialogData = MainDialogData(),
            listOfPlayers = mainState.value.listOfPlayers,
            currentMonth = mainState.value.currentMonth,
            listOfWinners = mainState.value.listOfWinners
        )
    }

    override fun closeDeletePlayerDialog(user: User) {
        mutableMainState.value = MainData(
            INIT,
            MainCardPlayerData(user.name, user.points != ZERO_POINT),
            MainDialogData(),
            mainState.value.listOfPlayers,
            mainState.value.currentMonth
        )
    }

    data class MainData(
        val state: MainStatus,
        val mainCard: MainCardPlayerData,
        val dialogData: MainDialogData,
        val listOfPlayers: ArrayList<User>,
        val currentMonth: String,
        val showMoreOptions: Boolean = false,
        val tabState: String = ACTUAL,
        val listOfWinners: ArrayList<WinnerMonth> = arrayListOf(),
        val openMonthCard: String = EMPTY_STRING,
        val deleteDialogName: User = User(EMPTY_STRING)
    )

    data class MainDialogData(
        val isEmptyDialogInputText: Boolean = false,
        val showErrorText: Boolean = false,
        val labelColor: Color = whiteBackgroundColor
    )

    data class MainCardPlayerData(
        val nameOfCardToOpen: String = EMPTY_STRING,
        val enableButton: Boolean = true
    )

    enum class MainStatus {
        INIT,
        SHOW_DIALOG,
        SHOW_DELETE_PLAYER_DIALOG,
        FINISH
    }
}
