package com.excu_fcd.filemanagerclient.mvvm.filemanager

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.MES
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.RES
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.WES
import com.excu_fcd.filemanagerclient.mvvm.filemanager.component.LocalItem
import com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state.LocalDisplayStateModel
import com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state.LocalLoadingStateModel
import com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state.LocalRequirePermissionsStateModel
import com.excu_fcd.filemanagerclient.mvvm.ui.component.ClickableIcon
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalMaterialApi::class,
    androidx.compose.animation.ExperimentalAnimationApi::class,
    ExperimentalPermissionsApi::class)
@Composable
fun FileManagerScreen(
    viewModel: FileManagerViewModel = hiltViewModel(),
) {
    val state by viewModel.files.collectAsState()
    var doNotShowAgain by rememberSaveable {
        mutableStateOf(false)
    }

    val permissions =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) rememberMultiplePermissionsState(
            permissions = listOf(
                MES
            ))
        else rememberMultiplePermissionsState(permissions = listOf(
            RES, WES
        ))


    val displayContent by remember {
        mutableStateOf(true)
    }

    Scaffold(
        contentColor = Color.Black,
        backgroundColor = Color.LightGray,
        topBar = {
            TopAppBar(
                contentColor = Color.Black,
                backgroundColor = Color.White,
                title = {
                    Text(text = "File manager")
                },
                navigationIcon = {
                    ClickableIcon(icon = Icons.Outlined.Menu) {

                    }
                },
                actions = {
                    ClickableIcon(icon = Icons.Outlined.Search) {

                    }
                    ClickableIcon(icon = Icons.Outlined.MoreVert) {

                    }
                }
            )
        },
        floatingActionButton = {
            if (state is LocalLoadingStateModel) {
                FloatingActionButton(onClick = { },
                    contentColor = Color.Black,
                    backgroundColor = Color.White) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                }
            }
        }
    ) {
        AnimatedVisibility(visible = displayContent) {
            when (state) {
                is LocalLoadingStateModel -> {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator()
                    }
                }

                is LocalRequirePermissionsStateModel -> {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    val uri = Uri.fromParts("package", LocalContext.current.packageName, null)
                    intent.data = uri
                    val context = LocalContext.current

                    PermissionsRequired(multiplePermissionsState = permissions,
                        permissionsNotGrantedContent = {

                        },
                        permissionsNotAvailableContent = {
                            Text(text = "This app needs some file perms for work. Open settings for grant them")
                        }) {
                        viewModel.reload()
                    }
                }

                is LocalDisplayStateModel -> {
                    val list = (state as LocalDisplayStateModel).items
                    LazyColumn {
                        items(list) {
                            LocalItem(model = it) {

                            }
                        }
                    }
                }

//                is LocalErrorStateModel -> {
//                    val reason = (state as LocalErrorStateModel).reason
//
//                    Surface(
//                        color = Color.White,
//                        contentColor = Color.Black
//                    ) {
//                        Column(modifier = Modifier.fillMaxSize(),
//                            verticalArrangement = Arrangement.Center,
//                            horizontalAlignment = Alignment.CenterHorizontally) {
//                            Icon(imageVector = Icons.Outlined.SentimentDissatisfied,
//                                contentDescription = null, modifier = Modifier.size(48.dp))
//
//                            Spacer(modifier = Modifier.size(8.dp))
//
//                            Text(text = "Can't get files", style = MaterialTheme.typography.h4)
//
//                            Spacer(modifier = Modifier.size(8.dp))
//
//                            Text(text = "Reason: $reason",
//                                style = MaterialTheme.typography.subtitle1)
//
//                            Spacer(modifier = Modifier.size(20.dp))
//
//                            OutlinedButton(onClick = { },
//                                colors = ButtonDefaults.outlinedButtonColors(
//                                    backgroundColor = MaterialTheme.colors.surface,
//                                    contentColor = Color.Black
//                                )) {
//                                Text(text = "Retry", color = Color.Black)
//                            }
//                        }
//
//                    }
//                }
            }
        }
    }
}
