package ru.bsu.application.dto;

public class RecyclerItemPoster {

    private String title;
    private String time;
    private String cost;
    private String location;
    private String reason;
    private String date;

    public RecyclerItemPoster() {
    }

    public RecyclerItemPoster(String title, String time, String cost, String location, String reason, String date) {
        this.title = title;
        this.time = time;
        this.cost = cost;
        this.location = location;
        this.reason = reason;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
