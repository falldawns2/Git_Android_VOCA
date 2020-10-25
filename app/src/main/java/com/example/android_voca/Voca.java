package com.example.android_voca;

public class Voca {

    // 단어 테이블 //

    int no; // 단어 무한 생성 번호 //
    int bbsid; // 게시판 번호 //
    String userid; // 아이디 //
    String VocaNoteName; // 단어장 명 //
    String ChapterName; // 챕터 명 //
    String Voca; // 단어 //
    String Mean; // 뜻 //
    String Sentence; // 예문 //
    String Interpretation; // 해석 //
    boolean Complete; // 무슨 목적이었는지 잊음 //
    String createDate; // 단어 생성 날짜 //

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getBbsid() {
        return bbsid;
    }

    public void setBbsid(int bbsid) {
        this.bbsid = bbsid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getVocaNoteName() {
        return VocaNoteName;
    }

    public void setVocaNoteName(String vocaNoteName) {
        VocaNoteName = vocaNoteName;
    }

    public String getChapterName() {
        return ChapterName;
    }

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }

    public String getVoca() {
        return Voca;
    }

    public void setVoca(String voca) {
        Voca = voca;
    }

    public String getMean() {
        return Mean;
    }

    public void setMean(String mean) {
        Mean = mean;
    }

    public String getSentence() {
        return Sentence;
    }

    public void setSentence(String sentence) {
        Sentence = sentence;
    }

    public String getInterpretation() {
        return Interpretation;
    }

    public void setInterpretation(String interpretation) {
        Interpretation = interpretation;
    }

    public boolean isComplete() {
        return Complete;
    }

    public void setComplete(boolean complete) {
        Complete = complete;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
