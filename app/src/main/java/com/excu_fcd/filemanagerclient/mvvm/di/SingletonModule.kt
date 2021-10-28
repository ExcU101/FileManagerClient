package com.excu_fcd.filemanagerclient.mvvm.di

import android.content.Context
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

//    @Provides
//    @Singleton
//    fun provideDataBase(@ApplicationContext context: Context): AbstractBase {
//        return Room.databaseBuilder(context, AbstractBase::class.java, "").build()
//    }


    @Provides
    @Singleton
    fun provideDocumentManager(@ApplicationContext context: Context): DocumentManager =
        DocumentManager(context = context)
}

