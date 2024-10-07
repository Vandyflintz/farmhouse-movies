package com.vandyflintz.farmhousemovies.model;

import java.util.ArrayList;

public class Movie {
    private String title, thumbnailUrl, id, duration, filename, description, moviedate, partid,
            favid, email, message, time, views, mpic, mcast,  fivestarraters, fourstarraters,  threestarraters
            ,  twostarraters,  onestarraters,  totalraters, overallrating, userrating, daterated,
            imageurl, contact, partname, remraters, movietype, seriesname, seriesid, movieid,
            seriesshortname, category, price, paidstatus, year, subscription, overallprice,
            expirydate, fexpirydate, fullmovieavailability, startdate, fstartdate, subtitle;


    private double rating;
    private ArrayList<String> genre;

    public Movie() {
    }

    public Movie(String name, String thumbnailUrl, String year, double rating, String id,
                 String duration, String filename, String description, String moviedate,
                 String partid, String favid, String email, String message, String time,
                 String views, String mpic, String mcast,String contact,
                 ArrayList<String> genre, String fivestarraters, String fourstarraters, String threestarraters
            , String twostarraters, String onestarraters, String totalraters,
                 String overallrating, String movietype,
                 String userrating, String daterated, String imageurl, String partname,
                 String remraters, String seriesname, String seriesid, String movieid,
                 String seriesshortname, String category, String price, String paidstatus,
                 String subscription, String overallprice, String expirydate, String startdate,
                 String fexpirydate, String fstartdate,
                 String fullmovieavailability, String subtitle) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
        this.id = id;
        this.duration = duration;
        this.filename = filename;
        this.description = description;
        this.moviedate = moviedate;
        this.partid= partid;
        this.favid = favid;
        this.email = email;
        this.message = message;
        this.time = time;
        this.views = views;
        this.mpic = mpic;
        this.mcast = mcast;
        this.fivestarraters = fivestarraters;
        this.fourstarraters = fourstarraters;
        this.threestarraters = threestarraters;
        this.twostarraters = twostarraters;
        this.onestarraters = onestarraters;
        this.totalraters = totalraters;
        this.overallrating = overallrating;
        this.userrating = userrating;
        this.daterated = daterated;
        this.imageurl = imageurl;
        this.contact = contact;
        this.partname = partname;
        this.remraters = remraters;
        this.movietype = movietype;
        this.seriesname = seriesname;
        this.seriesid = seriesid;
        this.movieid = movieid;
        this.seriesshortname = seriesshortname;
        this.category = category;
        this.price = price;
        this.paidstatus = paidstatus;
        this.subscription = subscription;
        this.overallprice = overallprice;
        this.expirydate = expirydate;
        this.startdate = startdate;
        this.fexpirydate = fexpirydate;
        this.fstartdate = fstartdate;
        this.fullmovieavailability = fullmovieavailability;
        this.subtitle = subtitle;
    }

    public String getOverallprice(){
        return overallprice;
    }

    public void setFullmovieavailability(String fma){
        this.fullmovieavailability = fma;
    }

    public String getFullmovieavailability(){
        return fullmovieavailability;
    }

    public void setExpirydate(String fep){
        this.expirydate = fep;
    }

    public String getExpirydate(){
        return expirydate;
    }

    public void setStartdate(String fsp){
        this.startdate = fsp;
    }

    public String getStartdate(){
        return startdate;
    }

    public void setFormattedExpirydate(String ffep){
        this.fexpirydate = ffep;
    }

    public String getFormattedExpirydate(){
        return fexpirydate;
    }

    public void setFormattedStartdate(String ffsp){
        this.fstartdate = ffsp;
    }

    public String getFormattedStartdate(){
        return fstartdate;
    }


    public void setOverallprice(String fop){
        this.overallprice = fop;
    }

    public String getSubscription(){
        return subscription;
    }

    public void setSubscription(String fscrub){
        this.subscription = fscrub;
    }

    public String getPaidstatus(){
        return paidstatus;
    }

    public void setPaidstatus(String fpaid){
        this.paidstatus = fpaid;
    }

    public String getPrice(){
        return price;
    }

    public void setPrice(String fprice){
        this.price = fprice;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String fcat){
        this.category = fcat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getSubTitle() {
        return subtitle;
    }

    public void setSubTitle(String name) {
        this.subtitle = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMovieType() {
        return movietype;
    }

    public void setMovietype(String ftype) {
        this.movietype = ftype;
    }

    public String getPictureImageurl() {
        return imageurl;
    }

    public void setPictureImageurl(String fUrl) {
        this.imageurl = fUrl;
    }

    public double getRating() {
        return rating;
    }

    public String getContact(){
        return contact;
    }

    public void setContact(String fcon){
        this.contact = fcon;
    }

    public String getRemraters(){
        return remraters;
    }

    public void setRemraters(String frem){
        this.remraters = frem;
    }

    public String getPartname(){
        return partname;
    }

    public void setPartname(String pname){
        this.partname = pname;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

    public String getID() {
        return id;
    }

    public void setID(String val){
        this.id = val;

    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String dval){
        this.duration = dval;

    }

    public String getFilename(){
        return filename;
    }

    public void setFilename(String fval){
        this.filename = fval;
    }


    public String getDesc(){
        return description;
    }

    public void setDesc(String fdesc){
        this.description = fdesc;
    }

    public String getDate(){
        return moviedate;
    }

    public void setDate(String fdate){
        this.moviedate = fdate;
    }

    public String getPic(){
        return mpic;
    }

    public void setPic(String fpic){
        this.mpic = fpic;
    }

    public String getCast(){
        return mcast;
    }

    public void setCast(String fcast){
        this.mcast = fcast;
    }

    public String getpartID(){
        return partid;
    }

    public void setpartID(String fpid){
        this.partid = fpid;
    }

    public  String getFav(){return favid;}

    public void setFav(String favstatus) {
    this.favid = favstatus;

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

    public String getFivestarraters(){
        return fivestarraters;
    }

    public void setFivestarraters(String fivestarraters) {
        this.fivestarraters = fivestarraters;
    }


    public String getFourstarraters(){
        return fourstarraters;
    }

    public void setFourstarraters(String fourstarraters) {
        this.fourstarraters = fourstarraters;
    }

    public String getThreestarraters(){
        return threestarraters;
    }

    public void setThreestarraters(String threestarraters) {
        this.threestarraters = threestarraters;
    }

    public String getTwostarraters(){
        return twostarraters;
    }

    public void setTwostarraters(String twostarraters) {
        this.twostarraters = twostarraters;
    }

    public String getOnestarraters(){
        return onestarraters;
    }

    public void setOnestarraters(String onestarraters) {
        this.onestarraters = onestarraters;
    }

    public String getTotalraters(){
        return totalraters;
    }

    public void setTotalraters(String totalraters) {
        this.totalraters = totalraters;
    }

    public String getUserrating(){
        return userrating;
    }

    public void setUserrating(String userrating) {
        this.userrating = userrating;
    }

    public String getOverallrating(){
        return overallrating;
    }

    public void setOverallrating(String overallrating) {
        this.overallrating = overallrating;
    }

    public String getDaterated(){
        return daterated;
    }

    public void setDaterated(String daterated) {
        this.daterated = daterated;
    }

    public String getView(){
        return views;
    }

    public void setView(String views) {
        this.views = views;
    }

    public String getSeriesName() {
        return seriesname;
    }

    public void setSeriesName(String fseriesname) {
        this.seriesname = fseriesname;
    }

    public String getSeriesShortName() {
        return seriesshortname;
    }

    public void setSeriesShortName(String fseriesshortname) {
        this.seriesshortname = fseriesshortname;
    }

    public String getSeriesID() {
        return seriesid;
    }

    public void setSeriesID(String fseriesid) {
        this.seriesid = fseriesid;
    }

    public void setMovieID(String fmovieid){
        this.movieid = fmovieid;
    }

    public String getMovieID() {
        return movieid;
    }
}

