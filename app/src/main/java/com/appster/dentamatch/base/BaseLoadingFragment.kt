package com.appster.dentamatch.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import javax.inject.Inject

open class BaseLoadingFragment<T : BaseLoadingViewModel> : BaseFragment() {

    override fun getFragmentName(): String = javaClass.simpleName

    @Inject
    lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private val errorObserver = Observer<Throwable> { e -> showSnackBar(e?.localizedMessage) }
}