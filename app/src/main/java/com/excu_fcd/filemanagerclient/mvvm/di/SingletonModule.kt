package com.excu_fcd.filemanagerclient.mvvm.di

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.excu_fcd.core.data.model.DocumentModel
import com.excu_fcd.core.extensions.asDocumentModel
import com.excu_fcd.core.provider.DocumentManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [])
@InstallIn(SingletonComponent::class)
object SingletonModule {
    @Provides
    @Singleton
    fun provideDocumentManager(@ApplicationContext context: Context): DocumentManager =
        DocumentManager(context)
}

