package com.example.mtimeapp.Bean;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class Film {
    private String film_com_id;
    private String filmTitle;
    private String filmSubTitle;
    private String aut_id;
    private String aut_name;
    private String aut_head;
    private String comment_num;
    private String filmImage;
    public Film(){}
    public String getFilm_com_id(){ return film_com_id;}

    public void setFilm_com_id(String film_com_id){this.film_com_id = film_com_id;}

    public String getFilmTitle(){return filmTitle;}

    public void setFilmTitle(String filmTitle){this.filmTitle = filmTitle;}

    public String getFilmSubTitle(){return filmSubTitle;}

    public void setFilmSubTitle(String filmSubTitle){this.filmSubTitle = filmSubTitle;}

    public String getAut_id(){return aut_id;}

    public void setAut_id(String aut_id){this.aut_id = aut_id;}

    public String getAut_name(){return aut_name;}

    public void setAut_name(String aut_name){this.aut_name= aut_name;}

    public String getAut_head(){return aut_head;}

    public void setAut_head(String aut_head){this.aut_head = aut_head;}

    public String getComment_num(){return comment_num;}

    public void setComment_num(String comment_num){this.comment_num = comment_num;}

    public String getFilmImage(){return filmImage;}

    public void setFilmImage(String filmImage){this.filmImage = filmImage;}

}
