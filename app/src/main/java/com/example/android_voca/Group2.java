package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

public class Group2 {

    //그룹

    //담아와야할 값들

    //그룹 명 //groupName
    //그룹 이미지 //groupImage

    @SerializedName("Page_NO")
    int Page_NO;

    @SerializedName("Page_SIZE")
    int Page_SIZE;

    // -------------------------------- //
    @SerializedName("GroupName")
    String GroupName;

    @SerializedName("GroupImage")
    String GroupImage;

    public Group2(int Page_NO, int Page_SIZE) {
        this.Page_NO = Page_NO;
        this.Page_SIZE = Page_SIZE;
    }

    public Group2(String GroupName, String GroupImage) {
        this.GroupName = GroupName;
        this.GroupImage = GroupImage;
    }

    public int getPage_NO() {
        return Page_NO;
    }

    public void setPage_NO(int page_NO) {
        Page_NO = page_NO;
    }

    public int getPage_SIZE() {
        return Page_SIZE;
    }

    public void setPage_SIZE(int page_SIZE) {
        Page_SIZE = page_SIZE;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupImage() {
        return GroupImage;
    }

    public void setGroupImage(String groupImage) {
        GroupImage = groupImage;
    }
}
