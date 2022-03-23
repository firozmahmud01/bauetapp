package com.firoz.mahmud.bauet;

public class ReportItem {
    public ReportItem(){}
    private String details,id,date;

    public ReportItem(String details, String id, String date) {
        this.details = details;
        this.id = id;
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
