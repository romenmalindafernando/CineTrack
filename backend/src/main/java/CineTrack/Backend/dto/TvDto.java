package CineTrack.Backend.dto;

public class TvDto {

    private int id;
    private String name;
    private String firstAirDate;
    private double rating;
    private String posterPath;
    private String overview;

    public TvDto() {
    }

    public TvDto(int id, String name, String firstAirDate,
                 double rating, String posterPath, String overview) {
        this.id = id;
        this.name = name;
        this.firstAirDate = firstAirDate;
        this.rating = rating;
        this.posterPath = posterPath;
        this.overview = overview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}