package com.example.mtimeapp.Bean;

public class Show {
    //"num":"数量(int)",
    //        "list":[{
    //        "title":"电影标题",
    //                "image":"缩略图",
    //                "info":"简介",
    //                "release_date":"上映时间",
    //                "film_id":"电影ID",
    //                "mark": "评分"
    //    }],
    //            "status": "ok"

    private String showTitle;
    private String showImage;
    private String showInfo;
    private String showRelease_time;
    private String showFilm_id;
    private String showMark;
    public Show(){}

    public String getShowTitle(){return showTitle;}

    public void setShowTitle(String showTitle){ this.showTitle = showTitle; }

    public String getShowImage(){return showImage;}

    public void setShowImage(String showImage){
        this.showImage = showImage;
    }

    public String getShowInfo(){return showInfo;}

    public void setShowInfo(String showInfo){
        this.showInfo = showInfo;
    }

    public String getShowRelease_time(){return showRelease_time;}

    public void setShowRelease_time(String showRelease_time){
        this.showRelease_time = showRelease_time;
    }

    public String getShowFilm_id(){return showFilm_id;}

    public void setShowFilm_id(String showFilm_id){
        this.showFilm_id = showFilm_id;
    }

    public String getShowMark(){return showMark;}

    public void setShowMark(String showMark){
        this.showMark = showMark;
    }
}
