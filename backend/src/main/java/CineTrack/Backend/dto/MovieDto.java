package CineTrack.Backend.dto;

public class MovieDto {

    private int id;
    private String title;
    private String releaseDate;
    private double rating;
    private String posterPath;
    private String overview;

    public MovieDto() {
    }

    public MovieDto(int id, String title, String releaseDate,
                    double rating, String posterPath, String overview) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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