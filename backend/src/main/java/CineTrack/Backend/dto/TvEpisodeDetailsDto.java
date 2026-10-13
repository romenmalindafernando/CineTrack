package CineTrack.Backend.dto;

public class TvEpisodeDetailsDto {

    private int id;
    private String name;
    private String overview;
    private int seasonNumber;
    private int episodeNumber;
    private String airDate;
    private String stillPath;
    private double rating;
    private Integer runtimeMinutes;

    public TvEpisodeDetailsDto() {
    }

    public TvEpisodeDetailsDto(
            int id,
            String name,
            String overview,
            int seasonNumber,
            int episodeNumber,
            String airDate,
            String stillPath,
            double rating,
            Integer runtimeMinutes) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.airDate = airDate;
        this.stillPath = stillPath;
        this.rating = rating;
        this.runtimeMinutes = runtimeMinutes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public int getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(int seasonNumber) { this.seasonNumber = seasonNumber; }

    public int getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(int episodeNumber) { this.episodeNumber = episodeNumber; }

    public String getAirDate() { return airDate; }
    public void setAirDate(String airDate) { this.airDate = airDate; }

    public String getStillPath() { return stillPath; }
    public void setStillPath(String stillPath) { this.stillPath = stillPath; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public Integer getRuntimeMinutes() { return runtimeMinutes; }
    public void setRuntimeMinutes(Integer runtimeMinutes) { this.runtimeMinutes = runtimeMinutes; }
}
