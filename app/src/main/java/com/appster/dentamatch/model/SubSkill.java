package com.appster.dentamatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ramkumar on 12/01/17.
 */

public class SubSkill implements Parcelable{
    private int id;
    private int parentId;
    private String skillName;
    @SerializedName("userSkill")
    private int isSelected;

    protected SubSkill(Parcel in) {
        id = in.readInt();
        parentId = in.readInt();
        skillName = in.readString();
        isSelected = in.readInt();
    }

    public static final Creator<SubSkill> CREATOR = new Creator<SubSkill>() {
        @Override
        public SubSkill createFromParcel(Parcel in) {
            return new SubSkill(in);
        }

        @Override
        public SubSkill[] newArray(int size) {
            return new SubSkill[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(parentId);
        dest.writeString(skillName);
        dest.writeInt(isSelected);
    }
}
