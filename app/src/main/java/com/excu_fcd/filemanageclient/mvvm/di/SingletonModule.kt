package com.excu_fcd.filemanageclient.mvvm.di

import android.content.Context
import com.excu_fcd.filemanageclient.mvvm.feature.manager.local.LocalManager
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
    fun provideLocalEmployerManager(@ApplicationContext context: Context): LocalManager =
        LocalManager(context = context)

    @Provides
    @Singleton
    fun getInt(): Int = 0
}

