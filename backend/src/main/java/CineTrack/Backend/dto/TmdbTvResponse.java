package CineTrack.Backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TmdbTvResponse {

    private int page;

    private List<TmdbTv> results;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_results")
    private int totalResults;

    public TmdbTvResponse() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<TmdbTv> getResults() {
        return results;
    }

    public void setResults(List<TmdbTv> results) {
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

    public static class TmdbTv {

        private int id;
        private String name;

        @JsonProperty("first_air_date")
        private String firstAirDate;

        @JsonProperty("vote_average")
        private double voteAverage;

        @JsonProperty("poster_path")
        private String posterPath;

        private String overview;

        public TmdbTv() {
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