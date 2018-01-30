package com.appster.dentamatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ramkumar on 12/01/17.
 */

public class SubSkillModel implements Parcelable {
    private int id;
    private int parentId;
    private String skillName;
    @SerializedName("userSkill")
    private int isSelected;
    private String otherSkill;
    private ArrayList<String> otherSkillArray;

    public SubSkillModel(){

    }
    protected SubSkillModel(Parcel in) {
        id = in.readInt();
        parentId = in.readInt();
        skillName = in.readString();
        isSelected = in.readInt();
        otherSkill = in.readString();
        otherSkillArray = in.createStringArrayList();
    }

    public static final Creator<SubSkillModel> CREATOR = new Creator<SubSkillModel>() {
        @Override
        public SubSkillModel createFromParcel(Parcel in) {
            return new SubSkillModel(in);
        }

        @Override
        public SubSkillModel[] newArray(int size) {
            return new SubSkillModel[size];
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

    public String getOtherText() {
        return otherSkill;
    }

    public void setOtherText(String otherText) {
        this.otherSkill = otherText;
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
        dest.writeString(otherSkill);
        dest.writeStringList(otherSkillArray);
    }
}
