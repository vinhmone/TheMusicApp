package com.chaubacho.themusicapp.data.source.local.utils

interface OnDataLocalCallback<T> {
    fun onSucceed(data: T)
    fun onFailed(e: Exception?)
}
