package com.example.mtimeapp.Bean;

public class Book {
    private String bookTitle;
    private String bookImage;
    private String bookInfo;
    private String bookRelease_time;
    private String bookMark;
    private String bookId;
    public Book(){}

    public String getBookTitle(){return bookTitle;}

    public void setBookTitle(String bookTitle){
        this.bookTitle = bookTitle;
    }

    public String getBookImage(){return bookImage;}

    public void setBookImage(String bookImage){
        this.bookImage = bookImage;
    }

    public String getBookInfo(){return bookInfo;}

    public void setBookInfo(String bookInfo){
        this.bookInfo = bookInfo;
    }

    public String getBookRelease_time(){return bookRelease_time;}

    public void setBookRelease_time(String bookRelease_time){
        this.bookRelease_time = bookRelease_time;
    }
    public String getBookId(){return bookId;}

    public void setBookId(String bookId){
        this.bookId = bookId;
    }

    public String getBookMark(){return bookMark;}

    public void setBookMark(String bookMark){
        this.bookMark = bookMark;
    }

//             title":"电影标题",
//            "image":"缩略图",
//            "info":"简介",
//            "release_date":"上映时间",
//            "film_id":"电影ID",
//            "mark": "评分"
}
