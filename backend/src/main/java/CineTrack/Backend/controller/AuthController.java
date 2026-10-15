package CineTrack.Backend.controller;

import CineTrack.Backend.dto.AuthResponse;
import CineTrack.Backend.dto.LoginRequest;
import CineTrack.Backend.dto.RegisterRequest;
import CineTrack.Backend.model.User;
import CineTrack.Backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest body,
            HttpServletRequest request) {
        try {
            User user = authService.register(body);
            establishSession(request, user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(user));
        } catch (AuthService.AccountConflictException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest body,
            HttpServletRequest request) {
        try {
            User user = authService.authenticate(body);
            establishSession(request, user);
            return ResponseEntity.ok(new AuthResponse(user));
        } catch (AuthService.InvalidCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "You are not logged in."));
        }

        Object userId = session.getAttribute("authenticatedUserId");
        if (!(userId instanceof String)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "You are not logged in."));
        }

        User user = authService.findAccountById((String) userId);
        if (user == null) {
            session.invalidate();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Your account could not be found."));
        }

        return ResponseEntity.ok(new AuthResponse(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(Map.of("message", "Logged out."));
    }

    private void establishSession(HttpServletRequest request, User user) {
        // Invalidate any existing session to reduce session-fixation risk.
        HttpSession existing = request.getSession(false);
        if (existing != null) {
            existing.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("authenticatedUserId", user.getId());
        session.setMaxInactiveInterval(30 * 60);
    }
}