package com.vandyflintz.farmhousemovies.model;

public class Message {

    private String title, email, message, time, movietype;

    public Message() {
    }

    public Message(String title, String email, String message, String time, String movietype) {
        this.title = title;
        this.email = email;
        this.message = message;
        this.title = time;
        this.movietype = movietype;
    }

    public String getMovieType() {
        return movietype;
    }

    public void setMovietype(String ftype) {
        this.movietype = ftype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String fmail){
        this.email = fmail;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String fmsg){
        this.message = fmsg;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String ftime){
        this.time = ftime;
    }

}
