package CineTrack.Backend.dto;

public class MonthlyStatsDTO {

    private String month;
    private long moviesWatched;
    private long tvSeriesWatched;
    private long episodesWatched;
    private long totalActivities;
    private long totalViewingMinutes;

    public MonthlyStatsDTO() {
    }

    public MonthlyStatsDTO(
            String month,
            long moviesWatched,
            long tvSeriesWatched,
            long episodesWatched,
            long totalActivities) {
        this(
                month,
                moviesWatched,
                tvSeriesWatched,
                episodesWatched,
                totalActivities,
                0
        );
    }

    public MonthlyStatsDTO(
            String month,
            long moviesWatched,
            long tvSeriesWatched,
            long episodesWatched,
            long totalActivities,
            long totalViewingMinutes) {
        this.month = month;
        this.moviesWatched = moviesWatched;
        this.tvSeriesWatched = tvSeriesWatched;
        this.episodesWatched = episodesWatched;
        this.totalActivities = totalActivities;
        this.totalViewingMinutes = totalViewingMinutes;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public long getEpisodesWatched() {
        return episodesWatched;
    }

    public void setEpisodesWatched(long episodesWatched) {
        this.episodesWatched = episodesWatched;
    }

    public long getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(long totalActivities) {
        this.totalActivities = totalActivities;
    }

    public long getTotalViewingMinutes() {
        return totalViewingMinutes;
    }

    public void setTotalViewingMinutes(long totalViewingMinutes) {
        this.totalViewingMinutes = totalViewingMinutes;
    }

    public double getTotalViewingHours() {
        return totalViewingMinutes / 60.0;
    }
}