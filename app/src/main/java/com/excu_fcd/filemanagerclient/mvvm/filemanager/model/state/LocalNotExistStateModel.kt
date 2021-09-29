package com.excu_fcd.filemanagerclient.mvvm.filemanager.model.state

import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager.Companion.SDCARD
import java.io.File

class LocalNotExistStateModel(file: File = SDCARD) : LocalStateModel {
}