package com.appster.dentamatch.base

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import javax.inject.Inject

@SuppressLint("Registered")
open class BaseLoadingActivity<T : BaseLoadingViewModel> : BaseActivity() {

    override fun getActivityName(): String = javaClass.simpleName

    @Inject
    lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loading.observe(this, mLoadingObserver)
        viewModel.error.observe(this, mErrorObserver)
    }

    private val mLoadingObserver = Observer<Boolean> { o ->
        if (true == o) {
            processToShowDialog()
        } else {
            hideProgressBar()
        }
    }

    private val mErrorObserver = Observer<Throwable> { e -> showSnackBar(e?.localizedMessage) }
}