package com.excu_fcd.core.extensions

import androidx.viewbinding.ViewBinding

interface FragmentBindingInitInterface<B : ViewBinding> {

    fun onCreateViewInit(binding: B) {}

}