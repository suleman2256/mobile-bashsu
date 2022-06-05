package ru.bsu.application.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poster {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("cost")
    @Expose
    private Integer cost;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("reasonCancellation")
    @Expose
    private String reasonCancellation;

    /**
     * No args constructor for use in serialization
     *
     */
    public Poster() {
    }

    /**
     *
     * @param date
     * @param cost
     * @param reasonCancellation
     * @param name
     * @param location
     * @param id
     * @param time
     */
    public Poster(Integer id, String name, String date, String time, Integer cost, String location, String reasonCancellation) {
        super();
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.location = location;
        this.reasonCancellation = reasonCancellation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReasonCancellation() {
        return reasonCancellation;
    }

    public void setReasonCancellation(String reasonCancellation) {
        this.reasonCancellation = reasonCancellation;
    }

}
