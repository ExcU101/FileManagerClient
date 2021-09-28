package com.excu_fcd.filemanagerclient.mvvm.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListDialog(show: Boolean, onDismissRequest: () -> Unit) {
    if (show) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                color = Color.White,
                contentColor = Color.Black,
            ) {
                Scaffold(modifier = Modifier.requiredHeight(356.dp),
                    topBar = {
                        TopAppBar(
                            contentColor = Color.Black,
                            backgroundColor = Color.White,
                            title = {
                                Text(text = "Pizdeс")
                            },
                            navigationIcon = {
                                IconButton(onClick = {}) {
                                    Icon(imageVector = Icons.Outlined.Close,
                                        contentDescription = null)
                                }
                            }
                        )
                    },
                    bottomBar = {
                        Column(
                            modifier = Modifier
                                .shadow(elevation = 8.dp)
                                .background(color = Color.White),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center) {
                                OutlinedTextField(shape = RoundedCornerShape(topStart = 0.dp,
                                    bottomStart = 0.dp, bottomEnd = 16.dp, topEnd = 16.dp),
                                    value = "",
                                    onValueChange = {

                                    },
                                    modifier = Modifier
                                        .weight(2F))
                                Spacer(modifier = Modifier.size(8.dp))
                                TextButton(onClick = { },
                                    modifier = Modifier
                                        .weight(1F)) {
                                    Text(text = "Add")
                                }
                            }
                            Button(shape = RoundedCornerShape(0.dp),
                                onClick = { },
                                modifier = Modifier.fillMaxWidth()) {
                                Text(text = "Apply")
                            }
                            Button(shape = RoundedCornerShape(0.dp),
                                onClick = { },
                                modifier = Modifier.fillMaxWidth()) {
                                Text(text = "Cancel")
                            }
                        }
                    }) {
                    LazyColumn(contentPadding = PaddingValues(bottom = 56.dp)) {
                        items(150) {
                            OneLineItem(text = "File $it", imageVector = Icons.Outlined.Delete)
                        }
                    }
                }
            }
        }
    }
}