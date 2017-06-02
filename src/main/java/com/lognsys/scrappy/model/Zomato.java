package com.lognsys.scrappy.model;

/**
 * POJO for Zomato Scraper
 *
 * Created by pdoshi on 2/19/16.
 */
public class Zomato {

    private long restID;
    private String restName = "";
    private String suburbArea = "";
    private String address = "";
    private String cuisine = "";
    private String cost = "";
    private double ratings = 0;
    private int votes = 0;
    private double lat = 0;
    private double lon = 0;
    private String reviews = "";
    private String timing;
    private String phoneNo;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public double getRatings() {
        return ratings;
    }

    public void setRatings(double ratings) {
        this.ratings = ratings;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public long getRestID() {
        return restID;
    }

    public void setRestID(long restID) {
        this.restID = restID;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getSuburbArea() {
        return suburbArea;
    }

    public void setSuburbArea(String suburbArea) {
        this.suburbArea = suburbArea;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Zomato{" +
                "restID=" + restID +
                ", restName='" + restName + '\'' +
                ", suburbArea='" + suburbArea + '\'' +
                ", address='" + address + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", cost='" + cost + '\'' +
                ", ratings=" + ratings +
                ", votes=" + votes +
                ", lat=" + lat +
                ", lon=" + lon +
                ", reviews='" + reviews + '\'' +
                ", timing='" + timing + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
