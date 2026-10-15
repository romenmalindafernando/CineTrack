package CineTrack.Backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CastMemberDto {
    private int id;
    private String name;
    private String character;

    @JsonProperty("profile_path")
    private String profilePath;

    public CastMemberDto() {}

    public CastMemberDto(
            int id,
            String name,
            String character,
            String profilePath) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.profilePath = profilePath;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCharacter() { return character; }
    public void setCharacter(String character) { this.character = character; }

    public String getProfilePath() { return profilePath; }
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
