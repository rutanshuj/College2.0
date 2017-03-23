package com.example.admin.college20;


/**
 * Created by RutanshuJhaveri on 2/5/2017.
 */

public class Event  {

    private String title, desc, location, category, price, imageUrl;

    private String start_date, end_date, start_time, end_time, fblink, weblink, contact, club;

    private String event_username, event_user_image;


    public Event(String title, String desc, String location, String category, String price,
                 String imageUrl, String start_date, String end_date, String start_time,
                 String end_time, String fblink, String weblink, String contact, String club,
                 String event_username, String event_user_image) {
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.fblink = fblink;
        this.weblink = weblink;
        this.contact = contact;
        this.club = club;
        this.event_username = event_username;
        this.event_user_image = event_user_image;
    }

    public Event(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getFblink() {
        return fblink;
    }

    public void setFblink(String fblink) {
        this.fblink = fblink;
    }

    public String getWeblink() {
        return weblink;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getEvent_username() {
        return event_username;
    }

    public void setEvent_username(String event_username) {
        this.event_username = event_username;
    }

    public String getEvent_user_image() {
        return event_user_image;
    }

    public void setEvent_user_image(String event_user_image) {
        this.event_user_image = event_user_image;
    }

}
