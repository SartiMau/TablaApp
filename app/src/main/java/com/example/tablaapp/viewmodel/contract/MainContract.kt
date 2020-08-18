package com.example.tablaapp.viewmodel.contract

interface MainContract {

    interface ViewModel {
        fun showDialogAddNewPlayer()
        fun onNavigationIconClicked()
        fun closeDialog()
        fun onConfirmDialogButton(textValue: String)
    }
}
