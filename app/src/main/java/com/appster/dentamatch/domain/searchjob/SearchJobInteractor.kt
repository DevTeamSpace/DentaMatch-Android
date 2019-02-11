package com.appster.dentamatch.domain.searchjob

import com.appster.dentamatch.model.JobTitleListModel
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import com.appster.dentamatch.util.Constants
import io.reactivex.Single
import java.util.*

class SearchJobInteractor(private val searchJobRemoteRepository: SearchJobRemoteRepository) {

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

}