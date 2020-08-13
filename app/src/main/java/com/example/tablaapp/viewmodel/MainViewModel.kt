package com.example.tablaapp.viewmodel

import androidx.compose.MutableState
import androidx.compose.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.ui.graphics.Color
import com.example.tablaapp.util.EMPTY_STRING
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.FINISH
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.INIT
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.SHOW_DIALOG
import com.example.tablaapp.viewmodel.contract.MainContract

class MainViewModel @ViewModelInject constructor() : ViewModel(), MainContract.ViewModel {

    private var mutableMainState: MutableState<MainData> = mutableStateOf(MainData(INIT, EMPTY_STRING, MainDialogData()))

    val mainState: MutableState<MainData>
        get() = mutableMainState


    override fun showDialogAddNewPlayer() {
        mainState.value.let {
            mutableMainState.value = MainData(SHOW_DIALOG, it.name, MainDialogData(it.dialogData.isEmptyDialogInputText))
        }
    }

    override fun onNavigationIconClicked() {
        mutableMainState.value = MainData(FINISH, EMPTY_STRING, MainDialogData())
    }

    override fun closeDialog() {
        mainState.let {
            mutableMainState.value = MainData(INIT, it.value.name, MainDialogData())
        }
    }

    override fun onConfirmDialogButton(textValue: String) {
        mainState.value.let {
            if (textValue.isEmpty()) {
                mutableMainState.value = MainData(
                    SHOW_DIALOG, it.name, MainDialogData(isEmptyDialogInputText = true, showErrorText = true, labelColor = Color.Red)
                )
            } else {
                mutableMainState.value = MainData(INIT, textValue, MainDialogData())
            }
        }
    }

    data class MainData(
        val state: MainStatus,
        val name: String,
        val dialogData: MainDialogData
    )

    data class MainDialogData(
        val isEmptyDialogInputText: Boolean = false,
        val showErrorText: Boolean = false,
        val labelColor: Color = Color.Unset
    )

    enum class MainStatus {
        INIT,
        SHOW_DIALOG,
        FINISH,
        CONFIRMATION_ERROR
    }
}