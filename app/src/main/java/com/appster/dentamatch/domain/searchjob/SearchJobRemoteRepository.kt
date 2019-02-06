package com.appster.dentamatch.domain.searchjob

import com.appster.dentamatch.base.RemoteRepositoryComposer
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel
import com.appster.dentamatch.network.response.profile.JobTitleResponse
import com.appster.dentamatch.util.PreferenceUtil
import io.reactivex.Single

class SearchJobRemoteRepository(
        private val composer: RemoteRepositoryComposer,
        private val searchJobRetrofit: SearchJobRetrofit
) {

    fun requestJobTitles(): Single<JobTitleResponse> =
            searchJobRetrofit.jobTitles()
                    .compose(composer.apiCompose())

    fun requestPreferredJobLocationList(): Single<PreferredJobLocationModel> =
            searchJobRetrofit.preferredJobLocationList()
                    .map { it.also { list -> PreferenceUtil.setPreferredJobList(list.result.preferredJobLocations) } }
                    .compose(composer.apiCompose())
}