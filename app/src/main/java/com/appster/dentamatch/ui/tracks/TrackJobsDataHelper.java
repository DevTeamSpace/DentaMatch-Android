package com.appster.dentamatch.ui.tracks;


/**
 * Created by Appster on 22/02/17.
 */

public class TrackJobsDataHelper {
    private static TrackJobsDataHelper mInstance;

    public static TrackJobsDataHelper getInstance(){
        if(mInstance == null){
            synchronized (TrackJobsDataHelper.class){
                if(mInstance == null){
                    mInstance = new TrackJobsDataHelper();
                }
            }
        }

        return mInstance;
    }

    private TrackJobsDataHelper(){

    }

    private void requestData(int TrackJobType){
        switch (TrackJobType){

        }
    }
}
