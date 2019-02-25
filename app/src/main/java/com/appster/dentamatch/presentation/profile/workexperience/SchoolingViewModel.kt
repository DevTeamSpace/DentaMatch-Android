package com.appster.dentamatch.presentation.profile.workexperience

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.domain.profile.ProfileInteractor
import com.appster.dentamatch.model.SchoolTypeModel
import com.appster.dentamatch.network.request.schools.PostSchoolData
import com.appster.dentamatch.network.response.schools.SchoolingResponse
import timber.log.Timber
import java.util.HashMap

class SchoolingViewModel(
        private val profileInteractor: ProfileInteractor
) : BaseLoadingViewModel() {

    private val mutableSchoolingList = MutableLiveData<SchoolingResponse>()
    private val mutableSchoolingError = MutableLiveData<Throwable>()
    private val mutableAddSchooling = MutableLiveData<BaseResponse>()

    val schoolingList: LiveData<SchoolingResponse> get() = mutableSchoolingList
    val schoolingError: LiveData<Throwable> get() = mutableSchoolingError
    val addSchooling: LiveData<BaseResponse> get() = mutableAddSchooling

    fun requestSchoolList() =
            addDisposable(
                    profileInteractor.requestSchoolList()
                            .compose(viewModelCompose())
                            .doOnError { mutableSchoolingError.postValue(it) }
                            .subscribe({ mutableSchoolingList.postValue(it) }, { Timber.e(it) })
            )

    fun addSchooling(postMapData: HashMap<Int, PostSchoolData>, list: MutableList<SchoolTypeModel>) =
            addDisposable(
                    profileInteractor.addSchooling(postMapData, list)
                            .compose(viewModelCompose())
                            .subscribe({ mutableAddSchooling.postValue(it) }, { Timber.e(it) })
            )
}