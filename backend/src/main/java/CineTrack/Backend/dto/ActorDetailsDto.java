package CineTrack.Backend.dto;

public class ActorDetailsDto {
    private int id;
    private String name;
    private String biography;
    private String birthday;
    private String placeOfBirth;
    private String profilePath;
    private String knownForDepartment;

    public ActorDetailsDto() {}

    public ActorDetailsDto(int id, String name, String biography,
            String birthday, String placeOfBirth, String profilePath,
            String knownForDepartment) {
        this.id = id;
        this.name = name;
        this.biography = biography;
        this.birthday = birthday;
        this.placeOfBirth = placeOfBirth;
        this.profilePath = profilePath;
        this.knownForDepartment = knownForDepartment;
    }

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
