package CineTrack.Backend.dto;

import java.util.List;

public class MovieDetailsDto {

    private int id;
    private String title;
    private String releaseDate;
    private double rating;
    private int runtime;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private List<String> genres;

    public MovieDetailsDto() {
    }

    public MovieDetailsDto(
            int id,
            String title,
            String releaseDate,
            double rating,
            int runtime,
            String posterPath,
            String backdropPath,
            String overview,
            List<String> genres) {

        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.runtime = runtime;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.genres = genres;
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

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}