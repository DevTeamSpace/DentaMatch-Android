/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network.retrofit;

import com.appster.dentamatch.network.request.jobs.SearchJobRequest;
import com.appster.dentamatch.network.response.jobs.SearchJobResponse;
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Interface to declare web service stubs.
 */
public interface AuthWebServices {

    String PAGE = "page";
    String TYPE = "type";
    String LAT = "lat";
    String LNG = "lng";
    String UNREAD_NOTIFICATION_COUNT = "users/unread-notification";
    String SEARCH_JOBS = "users/search-jobs";
    String TRACK_JOBS = "users/job-list";

    @GET(TRACK_JOBS)
    Call<SearchJobResponse> fetchTrackJobs(@Query(TYPE) int type,
                                           @Query(PAGE) int page,
                                           @Query(LAT) double lat,
                                           @Query(LNG) double lng);

    @POST(SEARCH_JOBS)
    Call<SearchJobResponse> searchJob(@Body SearchJobRequest request);

    @GET(UNREAD_NOTIFICATION_COUNT)
    Call<UnReadNotificationCountResponse> getUnreadNotificationCount();
}
