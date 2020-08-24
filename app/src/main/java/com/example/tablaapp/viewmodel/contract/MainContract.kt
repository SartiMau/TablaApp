package com.example.tablaapp.viewmodel.contract

import com.example.domain.entity.User

interface MainContract {

    interface ViewModel {
        fun showDialogAddNewPlayer()
        fun onNavigationIconClicked()
        fun closeDialog()
        fun onConfirmDialogButton(textValue: String)
        fun showCardButtons(player: User)
        fun addPointToPlayer(player: User)
        fun removePointToPlayer(player: User)
        fun showMoreOptions()
        fun resetPlayerPoints()
        fun showTabSelected(tabSelected: Int)
        fun showMonthCard(month: String)
    }
}
