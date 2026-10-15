package CineTrack.Backend.dto;

public class ActorCreditDto {
    private int id;
    private String title;
    private String mediaType;
    private String releaseDate;
    private String character;
    private double rating;
    private int voteCount;
    private String posterPath;
    private String overview;

    public ActorCreditDto() {}

    public ActorCreditDto(int id, String title, String mediaType,
            String releaseDate, String character, double rating,
            int voteCount, String posterPath, String overview) {
        this.id = id;
        this.title = title;
        this.mediaType = mediaType;
        this.releaseDate = releaseDate;
        this.character = character;
        this.rating = rating;
        this.voteCount = voteCount;
        this.posterPath = posterPath;
        this.overview = overview;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public String getCharacter() { return character; }
    public void setCharacter(String character) { this.character = character; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }
}
