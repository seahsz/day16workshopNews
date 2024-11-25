package vttp.ssf.day16workshop.models;

public class searchParam {

    public String query;
    public String country;
    public String category;

    public searchParam(String query, String country, String category) {
        this.query = query;
        this.country = country;
        this.category = category;
    }
}
