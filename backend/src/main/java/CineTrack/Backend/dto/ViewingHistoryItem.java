package CineTrack.Backend.dto;

public class ViewingHistoryItem {

    private int tmdbId;
    private String mediaType;
    private String watchedAt;
    private Integer seasonNumber;
    private Integer episodeNumber;
    private Integer runtimeMinutes;

    public ViewingHistoryItem() {
    }

    public ViewingHistoryItem(
            int tmdbId,
            String mediaType,
            String watchedAt,
            Integer seasonNumber,
            Integer episodeNumber) {
        this(
                tmdbId,
                mediaType,
                watchedAt,
                seasonNumber,
                episodeNumber,
                null
        );
    }

    public ViewingHistoryItem(
            int tmdbId,
            String mediaType,
            String watchedAt,
            Integer seasonNumber,
            Integer episodeNumber,
            Integer runtimeMinutes) {
        this.tmdbId = tmdbId;
        this.mediaType = mediaType;
        this.watchedAt = watchedAt;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.runtimeMinutes = runtimeMinutes;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(String watchedAt) {
        this.watchedAt = watchedAt;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Integer getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(Integer runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }
}