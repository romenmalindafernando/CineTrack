package CineTrack.Backend.model;

public class WatchlistItem {

    private int tmdbId;
    private String mediaType;
    private String addedAt;

    public WatchlistItem() {
    }

    public WatchlistItem(int tmdbId, String mediaType, String addedAt) {
        this.tmdbId = tmdbId;
        this.mediaType = mediaType;
        this.addedAt = addedAt;
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

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }
}