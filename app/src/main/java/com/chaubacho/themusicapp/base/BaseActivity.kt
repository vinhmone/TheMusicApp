package com.chaubacho.themusicapp.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.IllegalStateException

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {

    private var _viewBinding: V? = null
    protected abstract val bindingInflater: (LayoutInflater) -> V
    protected val viewBinding: V
        get() = _viewBinding
            ?: throw IllegalStateException(ERROR_VIEW_BINDING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = bindingInflater(layoutInflater)
        setContentView(_viewBinding!!.root)
        initComponents()
    }

    protected abstract fun initComponents()

    protected abstract fun initData()

    companion object {
        const val ERROR_VIEW_BINDING = "ViewBinding is null"
    }
}
