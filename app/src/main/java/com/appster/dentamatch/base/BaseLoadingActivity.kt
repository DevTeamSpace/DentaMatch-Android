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

    override fun onResume() {
        super.onResume()
        viewModel.loading.observe(this, loadingObserver)
        viewModel.error.observe(this, errorObserver)
    }

    override fun onPause() {
        viewModel.loading.removeObserver(loadingObserver)
        viewModel.error.removeObserver(errorObserver)
        super.onPause()
    }

    private val loadingObserver = Observer<Boolean> { o ->
        if (true == o) {
            processToShowDialog()
        } else {
            hideProgressBar()
        }
    }

    private val errorObserver = Observer<Throwable> { e -> showSnackBar(e?.localizedMessage) }
}