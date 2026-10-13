package CineTrack.Backend.model;

public class WatchedItem {

    private int tmdbId;
    private String mediaType;
    private String watchedAt;
    private Integer runtimeMinutes;

    public WatchedItem() {
    }

    public WatchedItem(int tmdbId, String mediaType, String watchedAt) {
        this(tmdbId, mediaType, watchedAt, null);
    }

    public WatchedItem(
            int tmdbId,
            String mediaType,
            String watchedAt,
            Integer runtimeMinutes) {
        this.tmdbId = tmdbId;
        this.mediaType = mediaType;
        this.watchedAt = watchedAt;
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

    public Integer getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(Integer runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }
}