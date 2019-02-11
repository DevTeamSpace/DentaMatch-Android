/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 21/01/17.
 * To inject activity reference.
 */
public class ProfileSubSkillModel implements Parcelable {
    @SerializedName("id")
    private int childId;
    @SerializedName("skillName")
    private String skillsChildName;
    private String otherSkill;

    public String getOtherSkills() {
        return otherSkill;
    }

    public int getChildId() {
        return childId;
    }

    public String getSkillsChildName() {
        return skillsChildName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.childId);
        dest.writeString(this.skillsChildName);
        dest.writeString(this.otherSkill);
    }

    public ProfileSubSkillModel() {
    }

    protected ProfileSubSkillModel(Parcel in) {
        this.childId = in.readInt();
        this.skillsChildName = in.readString();
        this.otherSkill = in.readString();
    }

    public static final Parcelable.Creator<ProfileSubSkillModel> CREATOR = new Parcelable.Creator<ProfileSubSkillModel>() {
        @Override
        public ProfileSubSkillModel createFromParcel(Parcel source) {
            return new ProfileSubSkillModel(source);
        }

        @Override
        public ProfileSubSkillModel[] newArray(int size) {
            return new ProfileSubSkillModel[size];
        }
    };
}
