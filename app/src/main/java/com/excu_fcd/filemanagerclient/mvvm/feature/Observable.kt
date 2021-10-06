package com.excu_fcd.filemanagerclient.mvvm.feature

interface Observable {

    fun notifyObserver(observer: List<Observer>)

}