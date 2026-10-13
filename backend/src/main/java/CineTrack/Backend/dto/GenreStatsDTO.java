
package CineTrack.Backend.dto;

public class GenreStatsDTO {

    private int genreId;
    private String genreName;
    private long moviesWatched;
    private long tvSeriesWatched;
    private long totalTitles;

    public GenreStatsDTO() {
    }

    public GenreStatsDTO(
            int genreId,
            String genreName,
            long moviesWatched,
            long tvSeriesWatched,
            long totalTitles) {

        this.genreId = genreId;
        this.genreName = genreName;
        this.moviesWatched = moviesWatched;
        this.tvSeriesWatched = tvSeriesWatched;
        this.totalTitles = totalTitles;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public long getMoviesWatched() {
        return moviesWatched;
    }

    public void setMoviesWatched(long moviesWatched) {
        this.moviesWatched = moviesWatched;
    }

    public long getTvSeriesWatched() {
        return tvSeriesWatched;
    }

    public void setTvSeriesWatched(long tvSeriesWatched) {
        this.tvSeriesWatched = tvSeriesWatched;
    }

    public long getTotalTitles() {
        return totalTitles;
    }

    public void setTotalTitles(long totalTitles) {
        this.totalTitles = totalTitles;
    }
}
