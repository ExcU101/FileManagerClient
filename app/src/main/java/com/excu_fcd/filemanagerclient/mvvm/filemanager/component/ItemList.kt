package com.excu_fcd.filemanagerclient.mvvm.filemanager.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.ui.component.RoundedFilledIcon

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocalItem(model: LocalUriModel, onClick: () -> Unit) {
//    var cornerRadius by remember {
//        mutableStateOf(0.dp)
//    }
    var selected by remember {
        mutableStateOf(false)
    }

    val cornerRadius by animateDpAsState(targetValue = if (selected) 16.dp else 0.dp)

    Card(
        shape = RoundedCornerShape(size = cornerRadius),
        onClick = {
            selected = !selected
        },
        modifier = Modifier
            .fillMaxSize()
    ) {
        ConstraintLayout(modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize()
        ) {
            val (title, icon, minInfo) = createRefs()

            RoundedFilledIcon(imageVector = if (model.isDirectory()) Icons.Outlined.Folder else Icons.Outlined.InsertDriveFile,
                modifier = Modifier.constrainAs(icon) {
                    start.linkTo(parent.start)
                    top.linkTo(title.top)
                    bottom.linkTo(minInfo.bottom)
                })

            Text(text = model.getName(),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(icon.end, margin = 16.dp)
                })

            Text(text = (if (model.isDirectory()) "Items: " else "Type: ") + model.getExtension(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.constrainAs(minInfo) {
                    top.linkTo(title.bottom)
                    start.linkTo(title.start)
                })
        }
    }
}