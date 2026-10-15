package CineTrack.Backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TmdbPersonDetailsResponse {
    private int id;
    private String name;
    private String biography;
    private String birthday;

    @JsonProperty("place_of_birth")
    private String placeOfBirth;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    public TmdbPersonDetailsResponse() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }
    public String getProfilePath() { return profilePath; }
    public void setProfilePath(String profilePath) { this.profilePath = profilePath; }
    public String getKnownForDepartment() { return knownForDepartment; }
    public void setKnownForDepartment(String knownForDepartment) { this.knownForDepartment = knownForDepartment; }
}
