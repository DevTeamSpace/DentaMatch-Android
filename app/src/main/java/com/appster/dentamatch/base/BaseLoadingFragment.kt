package com.appster.dentamatch.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import javax.inject.Inject

open class BaseLoadingFragment<T : BaseLoadingViewModel> : BaseFragment() {

    override fun getFragmentName(): String = javaClass.simpleName

    @Inject
    lateinit var viewModel: T

    override fun onPause() {
        viewModel.loading.removeObserver(loadingObserver)
        viewModel.error.removeObserver(errorObserver)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loading.observe(this, loadingObserver)
        viewModel.error.observe(this, errorObserver)
    }

    private val loadingObserver = Observer<Boolean> { o ->
        if (true == o) {
            showProgressBar()
        } else {
            hideProgressBar()
        }
    }

    private val errorObserver = Observer<Throwable> { e -> showToast(e?.localizedMessage) }
}