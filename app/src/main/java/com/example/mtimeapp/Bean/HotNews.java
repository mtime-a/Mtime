package com.example.mtimeapp.Bean;

public class HotNews {
    private String newsTitle;
    private String newsImage;
    private String pub_time;
    private int newsId;
    public HotNews(){}
    public String getNewsTitle(){return newsTitle;}

    public void setNewsTitle(String newsTitle){
        this.newsTitle = newsTitle;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) { this.newsImage = newsImage; }

    public String getPub_time(){return pub_time;}

    public void setPub_time(String pub_time) {this.pub_time = pub_time; }

    public int getNewsId(){return newsId;}

    public void setNewsId(int newsId){this.newsId = newsId;}

}
