package com.example.tablaapp.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import com.example.domain.entity.User
import com.example.tablaapp.ui.initScreen
import com.example.tablaapp.ui.showDialogAddNewPlayerScreen
import com.example.tablaapp.ui.showDialogDeletePlayerScreen
import com.example.tablaapp.viewmodel.MainViewModel
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.FINISH
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.INIT
import com.example.tablaapp.viewmodel.MainViewModel.MainStatus.SHOW_DELETE_PLAYER_DIALOG
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
                INIT -> init(it)
                SHOW_DIALOG -> showDialogAddNewPlayer(it)
                FINISH -> finish()
                SHOW_DELETE_PLAYER_DIALOG -> showDialogDeletePlayer(it, viewModel.mainState.value.deleteDialogName)
            }
        }
    }

    @Composable
    private fun init(context: Context) {
        initScreen(context, viewModel)
    }

    @Composable
    private fun showDialogAddNewPlayer(context: Context) {
        showDialogAddNewPlayerScreen(context, viewModel)
    }

    @Composable
    private fun showDialogDeletePlayer(context: Context, user: User) {
        showDialogDeletePlayerScreen(context, user, viewModel)
    }
}
