package CineTrack.Backend.model;

public class EpisodeProgress {

    private int tmdbId;
    private int seasonNumber;
    private int episodeNumber;
    private String watchedAt;
    private Integer runtimeMinutes;

    public EpisodeProgress() {
    }

    public EpisodeProgress(
            int tmdbId,
            int seasonNumber,
            int episodeNumber,
            String watchedAt) {
        this(tmdbId, seasonNumber, episodeNumber, watchedAt, null);
    }

    public EpisodeProgress(
            int tmdbId,
            int seasonNumber,
            int episodeNumber,
            String watchedAt,
            Integer runtimeMinutes) {
        this.tmdbId = tmdbId;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.watchedAt = watchedAt;
        this.runtimeMinutes = runtimeMinutes;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(String watchedAt) {
        this.watchedAt = watchedAt;
    }

    public Integer getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(Integer runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }
}