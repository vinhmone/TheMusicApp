package com.chaubacho.themusicapp.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.IllegalStateException

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {

    protected lateinit var viewBinding: V
    protected abstract val bindingInflater: (LayoutInflater) -> V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = bindingInflater(layoutInflater)
        setContentView(viewBinding.root)
        initComponents()
    }

    protected abstract fun initComponents()

}
