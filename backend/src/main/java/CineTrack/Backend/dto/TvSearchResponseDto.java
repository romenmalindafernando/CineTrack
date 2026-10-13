package CineTrack.Backend.dto;

import java.util.List;

public class TvSearchResponseDto {

    private int page;
    private int totalPages;
    private int totalResults;
    private List<TvDto> results;

    public TvSearchResponseDto() {
    }

    public TvSearchResponseDto(
            int page,
            int totalPages,
            int totalResults,
            List<TvDto> results) {

        this.page = page;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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

    public List<TvDto> getResults() {
        return results;
    }

    public void setResults(List<TvDto> results) {
        this.results = results;
    }
}