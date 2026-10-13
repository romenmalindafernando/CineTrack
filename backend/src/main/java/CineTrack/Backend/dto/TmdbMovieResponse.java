package CineTrack.Backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TmdbMovieResponse {

    private int page;

    private List<TmdbMovie> results;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_results")
    private int totalResults;

    public TmdbMovieResponse() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<TmdbMovie> getResults() {
        return results;
    }

    public void setResults(List<TmdbMovie> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public static class TmdbMovie {

        private int id;
        private String title;

        @JsonProperty("release_date")
        private String releaseDate;

        @JsonProperty("vote_average")
        private double voteAverage;

        @JsonProperty("poster_path")
        private String posterPath;

        private String overview;

        public TmdbMovie() {
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

        public double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
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
}