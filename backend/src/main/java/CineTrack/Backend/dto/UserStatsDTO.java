
package CineTrack.Backend.dto;

public class UserStatsDTO {

    private long moviesWatched;
    private long tvSeriesWatched;
    private long episodesWatched;
    private long moviesInWatchlist;
    private long tvSeriesInWatchlist;
    private long totalReviews;
    private double averageRating;

    public UserStatsDTO(
            long moviesWatched,
            long tvSeriesWatched,
            long episodesWatched,
            long moviesInWatchlist,
            long tvSeriesInWatchlist,
            long totalReviews,
            double averageRating) {

        this.moviesWatched = moviesWatched;
        this.tvSeriesWatched = tvSeriesWatched;
        this.episodesWatched = episodesWatched;
        this.moviesInWatchlist = moviesInWatchlist;
        this.tvSeriesInWatchlist = tvSeriesInWatchlist;
        this.totalReviews = totalReviews;
        this.averageRating = averageRating;
    }

    public long getMoviesWatched() {
        return moviesWatched;
    }

    public long getTvSeriesWatched() {
        return tvSeriesWatched;
    }

    public long getEpisodesWatched() {
        return episodesWatched;
    }

    public long getMoviesInWatchlist() {
        return moviesInWatchlist;
    }

    public long getTvSeriesInWatchlist() {
        return tvSeriesInWatchlist;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public double getAverageRating() {
        return averageRating;
    }
}
