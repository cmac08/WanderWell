package com.example.wanderwell.Search;

public abstract class SearchResult {
    public String title;
    public String type;
    public String dateInfo;
    public String hotelName;

    public abstract int getId(); // returns Id of the vacation or excursion

    public String getType() {
        return type; // returns "vacation" or "excursion"
    }

    public String getTitle() {
        return title;
    }

    public String getDateInfo() {
        return dateInfo;
    }

    public String getHotelName() {
        return hotelName;
    }
}
