package com.vandyflintz.farmhousemovies.model;

public class EMessage {

    private String title, email, message, time, thumbnailUrl, allnames, registerednames, contact,
            date, imageurl, movietype, seriesname, seriesid;

    public EMessage() {
    }

    public EMessage(String title, String email, String message, String time, String thumbnailUrl,
     String allnames, String registerednames, String contact, String date, String imageurl,
                    String movietype, String seriesname, String seriesid) {
        this.title = title;
        this.email = email;
        this.message = message;
        this.title = time;
        this.thumbnailUrl = thumbnailUrl;
        this.allnames = allnames;
        this.registerednames = registerednames;
        this.contact = contact;
        this.date = date;
        this.imageurl = imageurl;
        this.time = time;
        this.movietype = movietype;
        this.seriesname = seriesname;
        this.seriesid = seriesid;
    }

    public String getSeriesName() {
        return seriesname;
    }

    public void setSeriesName(String fseriesname) {
        this.seriesname = fseriesname;
    }

    public String getMovieType() {
        return movietype;
    }

    public void setMovietype(String ftype) {
        this.movietype = ftype;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String fUrl) {
        this.imageurl = fUrl;
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

    public String getDate(){
        return date;
    }

    public void setDate(String fdate){
        this.date = fdate;
    }

    public String getContact(){
        return contact;
    }

    public void setContact(String fcon){
        this.contact = fcon;
    }

    public String getRegisterednames(){
        return registerednames;
    }

    public void setRegisterednames(String frnames){
        this.registerednames = frnames;
    }

    public void setAllNames(String allnames) {
        this.allnames = allnames;
    }

    public String getAllNames() {
        return allnames;
    }

    public String getSeriesID() {
        return seriesid;
    }

    public void setSeriesID(String fseriesid) {
        this.seriesid = fseriesid;
    }
}
