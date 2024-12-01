package vttp.ssf.day16workshop.models;

public class SearchParam {

    public String query;
    public String country;
    public String category;

    public SearchParam(String query, String country, String category) {
        this.query = query;
        this.country = country;
        this.category = category;
    }

    public String getQuery() {
        return query;
    }

    public String getCountry() {
        return country;
    }

    public String getCategory() {
        return category;
    }


    
}
