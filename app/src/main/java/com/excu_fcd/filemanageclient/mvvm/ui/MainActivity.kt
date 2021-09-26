package com.excu_fcd.filemanageclient.mvvm.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.excu_fcd.filemanageclient.mvvm.ui.component.ListDialog
import com.excu_fcd.filemanageclient.mvvm.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private var mText = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val text by remember {
                mutableStateOf(mText)
            }

            MaterialTheme {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        mainViewModel.logAll()
                    },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    GetContent()
                }
            }
        }
    }

    @Composable
    fun GetContent() {
        var show by remember {
            mutableStateOf(false)
        }

        FloatingActionButton(onClick = {
            show = true
        }) {
            ListDialog(show) {
                show = !show
            }
        }

    }
}