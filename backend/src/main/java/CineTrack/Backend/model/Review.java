package CineTrack.Backend.model;

public class Review {

    private int tmdbId;
    private String mediaType;
    private double rating;
    private String review;
    private String createdAt;

    public Review() {
    }

    public Review(
            int tmdbId,
            String mediaType,
            double rating,
            String review,
            String createdAt) {

        this.tmdbId = tmdbId;
        this.mediaType = mediaType;
        this.rating = rating;
        this.review = review;
        this.createdAt = createdAt;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
