package CineTrack.Backend.dto;

import java.util.List;

public class YearlyStatsDTO {

    private int year;
    private long moviesWatched;
    private long tvSeriesWatched;
    private long episodesWatched;
    private long totalActivities;
    private long totalViewingMinutes;
    private List<MonthlyStatsDTO> monthlyBreakdown;

    public YearlyStatsDTO() {
    }

    public YearlyStatsDTO(
            int year,
            long moviesWatched,
            long tvSeriesWatched,
            long episodesWatched,
            long totalActivities,
            List<MonthlyStatsDTO> monthlyBreakdown) {
        this(
                year,
                moviesWatched,
                tvSeriesWatched,
                episodesWatched,
                totalActivities,
                0,
                monthlyBreakdown
        );
    }

    public YearlyStatsDTO(
            int year,
            long moviesWatched,
            long tvSeriesWatched,
            long episodesWatched,
            long totalActivities,
            long totalViewingMinutes,
            List<MonthlyStatsDTO> monthlyBreakdown) {
        this.year = year;
        this.moviesWatched = moviesWatched;
        this.tvSeriesWatched = tvSeriesWatched;
        this.episodesWatched = episodesWatched;
        this.totalActivities = totalActivities;
        this.totalViewingMinutes = totalViewingMinutes;
        this.monthlyBreakdown = monthlyBreakdown;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public List<MonthlyStatsDTO> getMonthlyBreakdown() {
        return monthlyBreakdown;
    }

    public void setMonthlyBreakdown(
            List<MonthlyStatsDTO> monthlyBreakdown) {
        this.monthlyBreakdown = monthlyBreakdown;
    }
}