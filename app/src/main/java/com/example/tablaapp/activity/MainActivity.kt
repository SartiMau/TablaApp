package com.example.tablaapp.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.setContent
import androidx.ui.layout.Column
import com.example.tablaapp.ui.showMainScreenContent
import com.example.tablaapp.ui.setToolbar
import com.example.tablaapp.ui.showCurrentMonth
import com.example.tablaapp.ui.showDialogAddNewPlayer
import com.example.tablaapp.viewmodel.MainViewModel
import com.example.tablaapp.viewmodel.MainViewModel.MainData
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.CONFIRMATION_ERROR
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.FINISH
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.INIT
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.SHOW_DIALOG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            updateUI()
        }
    }

    @Composable
    private fun updateUI() {
        applicationContext.let {
            when (viewModel.mainState.value.state) {
                INIT -> init(it, viewModel.mainState.value)
                SHOW_DIALOG -> showDialogAddNewPlayer(it, viewModel.mainState.value)
                FINISH -> finish()
                CONFIRMATION_ERROR -> showDialogAddNewPlayer(it, viewModel.mainState.value)
            }
        }
    }

    @Composable
    private fun init(context: Context, value: MainData) {
        Column {
            setToolbar(context, viewModel)
            showCurrentMonth(value.currentMonth)
            showMainScreenContent(value.listOfPlayers, viewModel)
        }
    }

    @Composable
    private fun showDialogAddNewPlayer(context: Context, value: MainData) {
        Column {
            setToolbar(context, viewModel)
            showCurrentMonth(value.currentMonth)
            showDialogAddNewPlayer(context, viewModel)
            showMainScreenContent(value.listOfPlayers, viewModel)
        }
    }
}
