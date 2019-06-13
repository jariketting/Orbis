package com.example.orbis;

public class SearchItems {
    private int mId;
    private String mImageResource;
    private String mText1;
    private String mText2;

    public SearchItems(int id, String imageResource, String text1, String text2){
        mId = id;
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
    }

    public int getId(){
        return mId;
    }
    public String getImageResource(){
        return mImageResource;
    }
    public String getText1(){
        return mText1;
    }
    public String getText2(){
        return mText2;
    }
}
