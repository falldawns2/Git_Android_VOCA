package com.example.android_voca;

import com.google.gson.annotations.SerializedName;
import kotlin.text.UStringsKt;

public class Board2{

    //게시판

    //담아와야할 값들
    //글 제목 //title
    //글 생성 날짜 //uploadtime
    //조회수 //hits
    //댓글 수 //comments
    //닉네임 //nickname

    @SerializedName("Page_NO")
    int Page_NO;

    @SerializedName("Page_SIZE")
    int Page_SIZE;

    //  --------------------------------  //
    @SerializedName("Profileimage")
    String Profileimage;

    @SerializedName("Title")
    String Title;

    @SerializedName("Uploadtime")
    String Uploadtime;

    @SerializedName("Hits")
    int Hits;

    @SerializedName("Comments")
    int Comments;

    @SerializedName("Nickname")
    String Nickname;

    public Board2(int Page_NO, int Page_SIZE) {
        this.Page_NO = Page_NO;
        this.Page_SIZE = Page_SIZE;
    }
    public Board2(String Profileimage, String Title, String Uploadtime, int Hits, int Comments, String Nickname) {
        this.Profileimage = Profileimage;
        this.Title = Title;
        this.Uploadtime = Uploadtime;
        this.Hits = Hits;
        this.Comments = Comments;
        this.Nickname = Nickname.trim();
    }
    public String getProfileimage() {
        return Profileimage;
    }

    public void setProfileimage(String profileimage) {
        Profileimage = profileimage;
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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUploadtime() {
        return Uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        Uploadtime = uploadtime;
    }

    public int getHits() {
        return Hits;
    }

    public void setHits(int hits) {
        Hits = hits;
    }

    public int getComments() {
        return Comments;
    }

    public void setComments(int comments) {
        Comments = comments;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname.trim();
    }
}
