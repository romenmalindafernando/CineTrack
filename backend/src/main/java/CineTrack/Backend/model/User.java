package CineTrack.Backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String username;
    private String email;

    private List<WatchlistItem> watchlist = new ArrayList<>();

    private List<WatchedItem> watched = new ArrayList<>();

    private List<Review> reviews = new ArrayList<>();

    private List<EpisodeProgress> watchedEpisodes = new ArrayList<>();

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.watchlist = new ArrayList<>();
        this.watched = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<WatchlistItem> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(List<WatchlistItem> watchlist) {
        this.watchlist = watchlist;
    }

    public List<WatchedItem> getWatched() {
        return watched;
    }

    public void setWatched(List<WatchedItem> watched) {
        this.watched = watched;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<EpisodeProgress> getWatchedEpisodes() {
        return watchedEpisodes;
    }

    public void setWatchedEpisodes(List<EpisodeProgress> watchedEpisodes) {
        this.watchedEpisodes = watchedEpisodes;
    }
}
