package com.excu_fcd.filemanagerclient.mvvm.di

import android.content.Context
import com.excu_fcd.filemanagerclient.mvvm.data.local.LocalUriModel
import com.excu_fcd.filemanagerclient.mvvm.data.request.Request
import com.excu_fcd.filemanagerclient.mvvm.feature.manager.local.LocalManager
import com.excu_fcd.filemanagerclient.mvvm.feature.worker.task.WorkDependency
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
    fun provideLocalWorkerDependency(request: Request<LocalUriModel>): WorkDependency =
        WorkDependency(request = request)
}

