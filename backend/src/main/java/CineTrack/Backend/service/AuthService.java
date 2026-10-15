package CineTrack.Backend.service;

import CineTrack.Backend.dto.LoginRequest;
import CineTrack.Backend.dto.RegisterRequest;
import CineTrack.Backend.model.User;
import CineTrack.Backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        String password = request.getPassword();

        if (username.length() < 3 || username.length() > 30) {
            throw new IllegalArgumentException("Username must be 3–30 characters.");
        }

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new AccountConflictException("That username is already registered.");
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new AccountConflictException("That email is already registered.");
        }

        // BCrypt accepts at most 72 password bytes, not 72 Unicode characters.
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException(
                    "Password is too long. Use at most 72 UTF-8 bytes.");
        }

        User user = new User(username, email);
        user.setPasswordHash(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public User authenticate(LoginRequest request) {
        String identifier = request.getIdentifier().trim();
        User user;

        if (identifier.contains("@")) {
            user = userRepository.findByEmailIgnoreCase(identifier).orElse(null);
        } else {
            user = userRepository.findByUsernameIgnoreCase(identifier).orElse(null);
        }

        if (user == null || user.getPasswordHash() == null ||
                !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return user;
    }

    public User findAccountById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public static class AccountConflictException extends RuntimeException {
        public AccountConflictException(String message) { super(message); }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException() {
            super("Invalid username/email or password.");
        }
    }
}