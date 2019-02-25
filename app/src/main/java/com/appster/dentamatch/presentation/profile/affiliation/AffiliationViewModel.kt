package com.appster.dentamatch.presentation.profile.affiliation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.eventbus.LocationEvent
import com.appster.dentamatch.network.response.affiliation.AffiliationResponse
import timber.log.Timber
import java.util.ArrayList

class AffiliationViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    private val mutableAffiliation = MutableLiveData<AffiliationResponse>()
    private val mutableFailedRequestAffiliation = MutableLiveData<Throwable>()
    private val mutableSaveAffiliations = MutableLiveData<BaseResponse>()

    val affiliation: LiveData<AffiliationResponse> get() = mutableAffiliation
    val failedRequestAffiliation: LiveData<Throwable> get() = mutableFailedRequestAffiliation
    val saveAffiliations: LiveData<BaseResponse> get() = mutableSaveAffiliations

    fun requestAffiliationList() =
            addDisposable(
                    profileInteractor.requestAffiliationList()
                            .compose(viewModelCompose())
                            .subscribe({ mutableAffiliation.postValue(it) }, { Timber.e(it) })
            )

    fun saveAffiliations(list: ArrayList<LocationEvent.Affiliation>) =
            addDisposable(
                    profileInteractor.saveAffiliations(list)
                            .compose(viewModelCompose())
                            .subscribe({ mutableSaveAffiliations.postValue(it) }, { Timber.e(it) })
            )
}