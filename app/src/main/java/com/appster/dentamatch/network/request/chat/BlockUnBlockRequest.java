package com.appster.dentamatch.network.request.chat;

/**
 * Created by Appster on 09/02/17.
 */

public class BlockUnBlockRequest  {
    private String recruiterId;
    private String blockStatus;

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public void setBlockStatus(String blockStatus) {
        this.blockStatus = blockStatus;
    }
}