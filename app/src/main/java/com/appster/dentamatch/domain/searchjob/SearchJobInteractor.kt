package com.appster.dentamatch.domain.searchjob

import android.content.Context
import com.appster.dentamatch.base.BaseResponse
import com.appster.dentamatch.model.JobTitleListModel
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import com.appster.dentamatch.network.response.jobs.JobDetailResponse
import com.appster.dentamatch.util.Constants
import com.appster.dentamatch.util.Utils
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class SearchJobInteractor(
        private val searchJobRemoteRepository: SearchJobRemoteRepository,
        private val context: Context
) {

    fun requestJobTitles(): Single<ArrayList<JobTitleListModel>> =
            searchJobRemoteRepository.requestJobTitles()
                    .map { it.jobTitleResponseData.jobTitleList }
                    .map {
                        it.apply {
                            for (i in it.indices) {
                                if (it[i].jobTitle.equals(Constants.OTHERS, true)) {
                                    if (i != it.size - 1) {
                                        Collections.swap(it, i, it.size - 1)
                                    }
                                    break
                                }
                            }
                        }
                    }

    fun requestPreferredJobLocationList(): Single<PreferredJobLocationModel> =
            searchJobRemoteRepository.requestPreferredJobLocationList()

    fun applyJob(jobId: Int): Single<BaseResponse> =
            searchJobRemoteRepository.applyJob(jobId)

    fun requestJobDetail(jobId: Int): Single<JobDetailResponse> =
            searchJobRemoteRepository.requestJobDetail(jobId)

    fun saveUnSaveJob(jobId: Int, status: Int): Completable =
            searchJobRemoteRepository.saveUnSaveJob(jobId, status)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { Utils.showToast(context, it.message) }
                    .ignoreElement()
}