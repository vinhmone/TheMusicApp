package com.chaubacho.themusicapp.data.source.local.utils

import java.lang.Exception

interface OnDataLocalCallback<T> {
    fun onSucceed(data: T)
    fun onFailed(e: Exception?)
}
