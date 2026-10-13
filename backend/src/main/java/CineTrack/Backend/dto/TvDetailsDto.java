package CineTrack.Backend.dto;

import java.util.List;

public class TvDetailsDto {

    private int id;
    private String name;
    private String firstAirDate;
    private double rating;
    private int numberOfSeasons;
    private int numberOfEpisodes;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private List<String> genres;

    public TvDetailsDto() {
    }

    public TvDetailsDto(
            int id,
            String name,
            String firstAirDate,
            double rating,
            int numberOfSeasons,
            int numberOfEpisodes,
            String posterPath,
            String backdropPath,
            String overview,
            List<String> genres) {

        this.id = id;
        this.name = name;
        this.firstAirDate = firstAirDate;
        this.rating = rating;
        this.numberOfSeasons = numberOfSeasons;
        this.numberOfEpisodes = numberOfEpisodes;
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

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
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