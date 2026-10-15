package CineTrack.Backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class TmdbCreditsResponse {
    private List<CastMemberDto> cast;

    public TmdbCreditsResponse() {}

    public List<CastMemberDto> getCast() {
        return cast;
    }

    public void setCast(List<CastMemberDto> cast) {
        this.cast = cast;
    }

    public static class CastMemberDto {
        private int id;
        private String name;
        private String character;

        @JsonProperty("profile_path")
        private String profilePath;

        public CastMemberDto() {}

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
}
