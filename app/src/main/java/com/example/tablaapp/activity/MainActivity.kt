package com.example.tablaapp.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.setContent
import com.example.tablaapp.ui.initScreen
import com.example.tablaapp.ui.showDialogAddNewPlayerScreen
import com.example.tablaapp.viewmodel.MainViewModel
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
                INIT -> init(it)
                SHOW_DIALOG -> showDialogAddNewPlayer(it)
                FINISH -> finish()
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
}
