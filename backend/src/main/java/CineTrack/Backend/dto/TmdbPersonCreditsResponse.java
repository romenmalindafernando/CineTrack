package CineTrack.Backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class TmdbPersonCreditsResponse {
    private List<Credit> cast;

    public TmdbPersonCreditsResponse() {}

    public List<Credit> getCast() { return cast; }
    public void setCast(List<Credit> cast) { this.cast = cast; }

    public static class Credit {
        private int id;
        private String title;
        private String name;

        @JsonProperty("media_type")
        private String mediaType;

        @JsonProperty("release_date")
        private String releaseDate;

        @JsonProperty("first_air_date")
        private String firstAirDate;

        private String character;

        @JsonProperty("vote_average")
        private double voteAverage;

        @JsonProperty("vote_count")
        private int voteCount;

        @JsonProperty("poster_path")
        private String posterPath;

        private String overview;

        public Credit() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getMediaType() { return mediaType; }
        public void setMediaType(String mediaType) { this.mediaType = mediaType; }
        public String getReleaseDate() { return releaseDate; }
        public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
        public String getFirstAirDate() { return firstAirDate; }
        public void setFirstAirDate(String firstAirDate) { this.firstAirDate = firstAirDate; }
        public String getCharacter() { return character; }
        public void setCharacter(String character) { this.character = character; }
        public double getVoteAverage() { return voteAverage; }
        public void setVoteAverage(double voteAverage) { this.voteAverage = voteAverage; }
        public int getVoteCount() { return voteCount; }
        public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
        public String getPosterPath() { return posterPath; }
        public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
        public String getOverview() { return overview; }
        public void setOverview(String overview) { this.overview = overview; }
    }
}
