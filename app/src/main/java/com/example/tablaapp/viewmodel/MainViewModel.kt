package com.example.tablaapp.viewmodel

import androidx.compose.MutableState
import androidx.compose.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.ui.graphics.Color
import com.example.domain.entity.User
import com.example.domain.util.ZERO_POINT
import com.example.tablaapp.util.EMPTY_STRING
import com.example.tablaapp.util.ONE_INT
import com.example.tablaapp.util.getMonth
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.FINISH
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.INIT
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.SHOW_DIALOG
import com.example.tablaapp.viewmodel.contract.MainContract

class MainViewModel @ViewModelInject constructor() : ViewModel(), MainContract.ViewModel {

    private var mutableMainState: MutableState<MainData> = mutableStateOf(
        MainData(INIT, MainCardPlayerData(), MainDialogData(), arrayListOf(), getMonth())
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
                MainDialogData(isEmptyDialogInputText = true, showErrorText = true, labelColor = Color.Red),
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
            MainCardPlayerData(mainState.value.mainCard.nameOfCardToOpen),
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
                mainState.value.mainCard.nameOfCardToOpen,
                mainState.value.listOfPlayers[position].points != ZERO_POINT
            ),
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
        val currentMonth: String
    )

    data class MainDialogData(
        val isEmptyDialogInputText: Boolean = false,
        val showErrorText: Boolean = false,
        val labelColor: Color = Color.Unset
    )

    data class MainCardPlayerData(
        val nameOfCardToOpen: String = EMPTY_STRING,
        val enableButton: Boolean = true
    )

    enum class MainStatus {
        INIT,
        SHOW_DIALOG,
        FINISH,
        CONFIRMATION_ERROR
    }
}
