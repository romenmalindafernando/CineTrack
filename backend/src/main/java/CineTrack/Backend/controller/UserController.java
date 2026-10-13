package CineTrack.Backend.controller;

import CineTrack.Backend.dto.YearlyStatsDTO;
import CineTrack.Backend.dto.MonthlyStatsDTO;
import CineTrack.Backend.dto.ViewingHistoryItem;
import CineTrack.Backend.dto.UserStatsDTO;
import CineTrack.Backend.model.User;
import CineTrack.Backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping("/{userId}/watchlist/{tmdbId}")
    public User addToWatchlist(
            @PathVariable String userId,
            @PathVariable int tmdbId,
            @RequestParam String mediaType) {

        return userService.addToWatchlist(
                userId,
                tmdbId,
                mediaType
        );
    }

    @DeleteMapping("/{userId}/watchlist/{tmdbId}")
    public User removeFromWatchlist(
            @PathVariable String userId,
            @PathVariable int tmdbId,
            @RequestParam String mediaType) {

        return userService.removeFromWatchlist(
                userId,
                tmdbId,
                mediaType
        );
    }

    @PostMapping("/{userId}/watched/{tmdbId}")
    public User addToWatched(
            @PathVariable String userId,
            @PathVariable int tmdbId,
            @RequestParam String mediaType) {

        return userService.addToWatched(
                userId,
                tmdbId,
                mediaType
        );
    }

    @DeleteMapping("/{userId}/watched/{tmdbId}")
    public User removeFromWatched(
            @PathVariable String userId,
            @PathVariable int tmdbId,
            @RequestParam String mediaType) {

        return userService.removeFromWatched(
                userId,
                tmdbId,
                mediaType
        );
    }

    @PostMapping("/{userId}/reviews")
    public User addReview(
            @PathVariable String userId,
            @RequestParam int tmdbId,
            @RequestParam String mediaType,
            @RequestParam double rating,
            @RequestParam String review) {

        return userService.addReview(
                userId,
                tmdbId,
                mediaType,
                rating,
                review
        );
    }

    @DeleteMapping("/{userId}/reviews/{tmdbId}")
    public User removeReview(
            @PathVariable String userId,
            @PathVariable int tmdbId,
            @RequestParam String mediaType) {

        return userService.removeReview(
                userId,
                tmdbId,
                mediaType
        );
    }

    @PostMapping("/{userId}/tv/{tmdbId}/seasons/{seasonNumber}/episodes/{episodeNumber}/watched")
    public User addWatchedEpisode(
            @PathVariable String userId,
            @PathVariable int tmdbId,
            @PathVariable int seasonNumber,
            @PathVariable int episodeNumber) {

        return userService.addWatchedEpisode(
                userId,
                tmdbId,
                seasonNumber,
                episodeNumber
        );
    }

    @DeleteMapping("/{userId}/tv/{tmdbId}/seasons/{seasonNumber}/episodes/{episodeNumber}/watched")
    public User removeWatchedEpisode(
            @PathVariable String userId,
            @PathVariable int tmdbId,
            @PathVariable int seasonNumber,
            @PathVariable int episodeNumber) {

        return userService.removeWatchedEpisode(
                userId,
                tmdbId,
                seasonNumber,
                episodeNumber
        );
    }

@GetMapping("/{userId}/stats")
public UserStatsDTO getUserStats(@PathVariable String userId) {
    return userService.getUserStats(userId);
}


@GetMapping("/{userId}/history")
public List<ViewingHistoryItem> getViewingHistory(
        @PathVariable String userId) {

    return userService.getViewingHistory(userId);
}


@GetMapping("/{userId}/stats/monthly")
public List<MonthlyStatsDTO> getMonthlyStats(
        @PathVariable String userId) {

    return userService.getMonthlyStats(userId);
}


@GetMapping("/{userId}/stats/yearly")
public YearlyStatsDTO getYearlyStats(
        @PathVariable String userId,
        @RequestParam int year) {

    return userService.getYearlyStats(userId, year);
}


}
