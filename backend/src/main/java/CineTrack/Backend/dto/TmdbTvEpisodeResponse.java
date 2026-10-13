package CineTrack.Backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TmdbTvEpisodeResponse {

    private int id;
    private String name;
    private String overview;
    private Integer runtime;

    @JsonProperty("season_number")
    private int seasonNumber;

    @JsonProperty("episode_number")
    private int episodeNumber;

    @JsonProperty("air_date")
    private String airDate;

    @JsonProperty("still_path")
    private String stillPath;

    @JsonProperty("vote_average")
    private double voteAverage;

    public TmdbTvEpisodeResponse() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public Integer getRuntime() { return runtime; }
    public void setRuntime(Integer runtime) { this.runtime = runtime; }

    public int getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(int seasonNumber) { this.seasonNumber = seasonNumber; }

    public int getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(int episodeNumber) { this.episodeNumber = episodeNumber; }

    public String getAirDate() { return airDate; }
    public void setAirDate(String airDate) { this.airDate = airDate; }

    public String getStillPath() { return stillPath; }
    public void setStillPath(String stillPath) { this.stillPath = stillPath; }

    public double getVoteAverage() { return voteAverage; }
    public void setVoteAverage(double voteAverage) { this.voteAverage = voteAverage; }
}
