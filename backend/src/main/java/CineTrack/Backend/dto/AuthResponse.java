package CineTrack.Backend.dto;

import CineTrack.Backend.model.User;

public class AuthResponse {
    private final String id;
    private final String username;
    private final String email;

    public AuthResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}