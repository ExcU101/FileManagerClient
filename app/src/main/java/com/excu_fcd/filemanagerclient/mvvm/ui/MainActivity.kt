package com.excu_fcd.filemanagerclient.mvvm.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.excu_fcd.filemanagerclient.mvvm.filemanager.FileManagerScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val uiController = rememberSystemUiController()


                SideEffect {
                    uiController.setStatusBarColor(color = Color.White, darkIcons = true)
                }

                NavHost(navController = navController, startDestination = "FILE_MANAGER") {
                    composable(route = "FILE_MANAGER") {
                        FileManagerScreen()
                    }
                }
            }
        }
    }
}