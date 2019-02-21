package com.appster.dentamatch.presentation.calendar

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.appster.dentamatch.base.BaseLoadingViewModel
import com.appster.dentamatch.domain.calendar.CalendarInteractor
import com.appster.dentamatch.network.request.calendar.GetAvailabilityRequest
import com.appster.dentamatch.network.request.calendar.SaveAvailabilityRequest
import com.appster.dentamatch.network.response.calendar.AvailabilityResponse
import timber.log.Timber

class SetAvailabilityViewModel(
        private val calendarInteractor: CalendarInteractor
) : BaseLoadingViewModel() {

    companion object {
        const val TAG = "SetAvailabilityViewMod"
    }

    private val mutableAvailabilityResponse = MutableLiveData<AvailabilityResponse>()
    private val mutableSaveAvailability = MutableLiveData<Boolean>()

    val availabilityResponse: LiveData<AvailabilityResponse> get() = mutableAvailabilityResponse
    val saveAvailability: LiveData<Boolean> get() = mutableSaveAvailability

    fun requestAvailabilityList(request: GetAvailabilityRequest) =
            addDisposable(
                    calendarInteractor.requestAvailabilityList(request)
                            .compose(viewModelCompose())
                            .subscribe({ mutableAvailabilityResponse.postValue(it) },
                                    { Timber.e(it) })
            )

    fun saveAvailability(request: SaveAvailabilityRequest) =
            addDisposable(
                    calendarInteractor.saveAvailability(request)
                            .compose(viewModelCompletableCompose())
                            .subscribe({ mutableSaveAvailability.postValue(true) },
                                    { Timber.e(it) })
            )
}