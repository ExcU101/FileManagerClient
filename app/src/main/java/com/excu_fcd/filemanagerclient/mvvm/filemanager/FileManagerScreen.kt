package com.excu_fcd.filemanagerclient.mvvm.filemanager

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.RES
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.WES
import com.excu_fcd.filemanagerclient.mvvm.filemanager.component.LocalHeader
import com.excu_fcd.filemanagerclient.mvvm.filemanager.component.LocalItem
import com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state.LocalDisplayStateModel
import com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state.LocalLoadingStateModel
import com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state.LocalRequirePermissionsStateModel
import com.excu_fcd.filemanagerclient.mvvm.ui.component.Action
import com.excu_fcd.filemanagerclient.mvvm.ui.component.SimpleToolbar
import com.excu_fcd.filemanagerclient.mvvm.ui.component.ThemedIconFab
import com.excu_fcd.filemanagerclient.mvvm.utils.largePadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalPermissionsApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun FileManagerScreen(
    viewModel: FileManagerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        contentColor = Color.Black,
        backgroundColor = Color.LightGray,
        topBar = {
            Column(Modifier.shadow(elevation = AppBarDefaults.TopAppBarElevation)) {
                SimpleToolbar(
                    titleText = "File Manager",
                    actions = listOf(
                        Action(name = "Search",
                            icon = rememberVectorPainter(Icons.Outlined.Search)) {
                        },
                        Action(name = "More",
                            icon = rememberVectorPainter(Icons.Outlined.MoreVert))
                    ))
            }

        },
        floatingActionButton = {
            ThemedIconFab()
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) {
        when (state) {
            is LocalLoadingStateModel -> {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    CircularProgressIndicator(modifier = Modifier, color = Color.Black)
                    Spacer(modifier = Modifier.size(largePadding))
                    Text(text = "Getting your files", style = MaterialTheme.typography.h6)
                }
            }

            is LocalRequirePermissionsStateModel -> {
                val context = LocalContext.current

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val intent =
                            Intent().apply {
                                action = ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                                data = Uri.fromParts("package", context.packageName, null)
                            }

                        Button(onClick = {
                            when {
                                Environment.isExternalStorageManager() -> viewModel.reload()
                                else -> context.startActivity(intent)
                            }
                        }) {
                            Text(text = "Open settings")
                        }


                    }
                    val permissions =
                        rememberMultiplePermissionsState(permissions = listOf(RES, WES))

                    when {
                        permissions.allPermissionsGranted -> {
                            viewModel.reload()
                        }

                        permissions.shouldShowRationale || !permissions.permissionRequested -> {
                            Button(onClick = { permissions.launchMultiplePermissionRequest() }) {
                                Text(text = "Show permissions")
                            }
                        }

                        else -> {
                            val intent =
                                Intent().apply {
                                    action = ACTION_APPLICATION_DETAILS_SETTINGS
                                    data = Uri.fromParts("package", context.packageName, null)
                                }

                            context.startActivity(intent)
                        }
                    }
                }

            }

            is LocalDisplayStateModel -> {
                val grouped = (state as LocalDisplayStateModel).items

                LazyColumn(contentPadding = PaddingValues(bottom = 72.dp), content = {
                    grouped.forEach { (header, list) ->
                        stickyHeader {
                            LocalHeader(isDirectory = header)
                        }

                        items(list, key = { item -> item.getName() }) { local ->
                            LocalItem(model = local) {

                            }
                        }
                    }
                })
            }
        }
    }
}
