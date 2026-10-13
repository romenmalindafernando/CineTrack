package CineTrack.Backend.service;

import CineTrack.Backend.dto.YearlyStatsDTO;
import CineTrack.Backend.dto.MovieDetailsDto;
import CineTrack.Backend.dto.TvEpisodeDetailsDto;
import org.springframework.web.client.RestClientException;
import CineTrack.Backend.dto.MonthlyStatsDTO;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.TreeMap;
import CineTrack.Backend.dto.ViewingHistoryItem;
import java.util.Comparator;
import CineTrack.Backend.dto.UserStatsDTO;
import CineTrack.Backend.model.Review;
import CineTrack.Backend.model.EpisodeProgress;
import CineTrack.Backend.model.User;
import CineTrack.Backend.model.WatchedItem;
import CineTrack.Backend.model.WatchlistItem;
import CineTrack.Backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MovieService movieService;
    private final TvService tvService;

    public UserService(
            UserRepository userRepository,
            MovieService movieService,
            TvService tvService) {
        this.userRepository = userRepository;
        this.movieService = movieService;
        this.tvService = tvService;
    }

    public User createUser(User user) {

        if (user.getWatchlist() == null) {
            user.setWatchlist(new ArrayList<>());
        }

        if (user.getWatched() == null) {
            user.setWatched(new ArrayList<>());
        }

        if (user.getReviews() == null) {
            user.setReviews(new ArrayList<>());
        }
        if (user.getWatchedEpisodes() == null) {
            user.setWatchedEpisodes(new ArrayList<>());
        }

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User addToWatchlist(
            String userId,
            int tmdbId,
            String mediaType) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        if (user.getWatchlist() == null) {
            user.setWatchlist(new ArrayList<>());
        }

        boolean alreadyExists = user.getWatchlist()
                .stream()
                .anyMatch(item ->
                        item.getTmdbId() == tmdbId
                                && item.getMediaType()
                                .equalsIgnoreCase(mediaType)
                );

        if (!alreadyExists) {

            WatchlistItem item = new WatchlistItem(
                    tmdbId,
                    mediaType,
                    LocalDateTime.now().toString()
            );

            user.getWatchlist().add(item);
        }

        return userRepository.save(user);
    }

    public User removeFromWatchlist(
            String userId,
            int tmdbId,
            String mediaType) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        if (user.getWatchlist() != null) {

            user.getWatchlist().removeIf(item ->
                    item.getTmdbId() == tmdbId
                            && item.getMediaType()
                            .equalsIgnoreCase(mediaType)
            );
        }

        return userRepository.save(user);
    }

    public User addToWatched(
            String userId,
            int tmdbId,
            String mediaType) {

        if (tmdbId <= 0) {
            throw new IllegalArgumentException("TMDB ID must be positive.");
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        if (user.getWatched() == null) {
            user.setWatched(new ArrayList<>());
        }

        WatchedItem existingItem = user.getWatched()
                .stream()
                .filter(item ->
                        item.getTmdbId() == tmdbId
                                && item.getMediaType() != null
                                && item.getMediaType().equalsIgnoreCase(mediaType)
                )
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            if ("movie".equalsIgnoreCase(mediaType)
                    && (existingItem.getRuntimeMinutes() == null
                    || existingItem.getRuntimeMinutes() <= 0)) {

                Integer runtimeMinutes = getMovieRuntimeMinutes(tmdbId);

                if (runtimeMinutes != null && runtimeMinutes > 0) {
                    existingItem.setRuntimeMinutes(runtimeMinutes);
                }
            }
        } else {
            Integer runtimeMinutes = null;

            if ("movie".equalsIgnoreCase(mediaType)) {
                runtimeMinutes = getMovieRuntimeMinutes(tmdbId);
            }

            WatchedItem item = new WatchedItem(
                    tmdbId,
                    mediaType,
                    LocalDateTime.now().toString(),
                    runtimeMinutes
            );

            user.getWatched().add(item);
        }

        return userRepository.save(user);
    }
    private Integer getMovieRuntimeMinutes(int tmdbId) {
        try {
            MovieDetailsDto details = movieService.getMovieDetails(tmdbId);

            if (details != null && details.getRuntime() > 0) {
                return details.getRuntime();
            }
        } catch (RestClientException exception) {
            // Keep the watch record even if TMDB is unavailable.
        }

        return null;
    }

    private Integer getEpisodeRuntimeMinutes(
            int tmdbId,
            int seasonNumber,
            int episodeNumber) {
        try {
            TvEpisodeDetailsDto details = tvService.getEpisodeDetails(
                    tmdbId, seasonNumber, episodeNumber
            );

            if (details != null
                    && details.getRuntimeMinutes() != null
                    && details.getRuntimeMinutes() > 0) {
                return details.getRuntimeMinutes();
            }
        } catch (RestClientException exception) {
            // Keep the watch record even if TMDB is unavailable.
        }

        return null;
    }

    public User removeFromWatched(
            String userId,
            int tmdbId,
            String mediaType) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        if (user.getWatched() != null) {

            user.getWatched().removeIf(item ->
                    item.getTmdbId() == tmdbId
                            && item.getMediaType()
                            .equalsIgnoreCase(mediaType)
            );
        }

        return userRepository.save(user);
    }

    public User addReview(
            String userId,
            int tmdbId,
            String mediaType,
            double rating,
            String reviewText) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        if (user.getReviews() == null) {
            user.setReviews(new ArrayList<>());
        }
        if (user.getWatchedEpisodes() == null) {
            user.setWatchedEpisodes(new ArrayList<>());
        }

        Review existingReview = user.getReviews()
                .stream()
                .filter(review ->
                        review.getTmdbId() == tmdbId
                                && review.getMediaType()
                                .equalsIgnoreCase(mediaType)
                )
                .findFirst()
                .orElse(null);

        if (existingReview != null) {

            existingReview.setRating(rating);
            existingReview.setReview(reviewText);

        } else {

            Review newReview = new Review(
                    tmdbId,
                    mediaType,
                    rating,
                    reviewText,
                    LocalDateTime.now().toString()
            );

            user.getReviews().add(newReview);
        }

        return userRepository.save(user);
    }

    public User removeReview(
            String userId,
            int tmdbId,
            String mediaType) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        if (user.getReviews() != null) {

            user.getReviews().removeIf(review ->
                    review.getTmdbId() == tmdbId
                            && review.getMediaType()
                            .equalsIgnoreCase(mediaType)
            );
        }

        return userRepository.save(user);
    }

    public User addWatchedEpisode(
            String userId,
            int tmdbId,
            int seasonNumber,
            int episodeNumber) {

        if (tmdbId <= 0 || seasonNumber <= 0 || episodeNumber <= 0) {
            throw new IllegalArgumentException(
                    "TMDB ID, season number, and episode number must be positive.");
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        if (user.getWatchedEpisodes() == null) {
            user.setWatchedEpisodes(new ArrayList<>());
        }

        EpisodeProgress existingEpisode = user.getWatchedEpisodes()
                .stream()
                .filter(item ->
                        item.getTmdbId() == tmdbId
                                && item.getSeasonNumber() == seasonNumber
                                && item.getEpisodeNumber() == episodeNumber
                )
                .findFirst()
                .orElse(null);

        if (existingEpisode != null) {
            if (existingEpisode.getRuntimeMinutes() == null
                    || existingEpisode.getRuntimeMinutes() <= 0) {

                Integer runtimeMinutes = getEpisodeRuntimeMinutes(
                        tmdbId, seasonNumber, episodeNumber
                );

                if (runtimeMinutes != null && runtimeMinutes > 0) {
                    existingEpisode.setRuntimeMinutes(runtimeMinutes);
                }
            }
        } else {
            Integer runtimeMinutes = getEpisodeRuntimeMinutes(
                    tmdbId, seasonNumber, episodeNumber
            );

            EpisodeProgress episode = new EpisodeProgress(
                    tmdbId,
                    seasonNumber,
                    episodeNumber,
                    LocalDateTime.now().toString(),
                    runtimeMinutes
            );

            user.getWatchedEpisodes().add(episode);
        }

        return userRepository.save(user);
    }
    public User removeWatchedEpisode(
            String userId,
            int tmdbId,
            int seasonNumber,
            int episodeNumber) {

        if (tmdbId <= 0 || seasonNumber <= 0 || episodeNumber <= 0) {
            throw new IllegalArgumentException(
                    "TMDB ID, season number, and episode number must be positive.");
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        if (user.getWatchedEpisodes() != null) {
            user.getWatchedEpisodes().removeIf(item ->
                    item.getTmdbId() == tmdbId
                            && item.getSeasonNumber() == seasonNumber
                            && item.getEpisodeNumber() == episodeNumber
            );
        }

        return userRepository.save(user);
    }

public UserStatsDTO getUserStats(String userId) {

    User user = userRepository.findById(userId).orElse(null);

    if (user == null) {
        return null;
    }

    long moviesWatched = user.getWatched() == null ? 0 :
            user.getWatched().stream()
                    .filter(item -> "movie".equalsIgnoreCase(item.getMediaType()))
                    .count();

    long tvSeriesWatched = user.getWatched() == null ? 0 :
            user.getWatched().stream()
                    .filter(item -> "tv".equalsIgnoreCase(item.getMediaType()))
                    .count();

    long episodesWatched = user.getWatchedEpisodes() == null ? 0 :
            user.getWatchedEpisodes().size();

    long moviesInWatchlist = user.getWatchlist() == null ? 0 :
            user.getWatchlist().stream()
                    .filter(item -> "movie".equalsIgnoreCase(item.getMediaType()))
                    .count();

    long tvSeriesInWatchlist = user.getWatchlist() == null ? 0 :
            user.getWatchlist().stream()
                    .filter(item -> "tv".equalsIgnoreCase(item.getMediaType()))
                    .count();

    long totalReviews = user.getReviews() == null ? 0 :
            user.getReviews().size();

    double averageRating = user.getReviews() == null
            || user.getReviews().isEmpty()
            ? 0.0
            : user.getReviews().stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);

    return new UserStatsDTO(
            moviesWatched,
            tvSeriesWatched,
            episodesWatched,
            moviesInWatchlist,
            tvSeriesInWatchlist,
            totalReviews,
            averageRating
    );
}


public List<ViewingHistoryItem> getViewingHistory(String userId) {

    User user = userRepository.findById(userId).orElse(null);

    if (user == null) {
        return null;
    }

    List<ViewingHistoryItem> history = new ArrayList<>();

    if (user.getWatched() != null) {
        user.getWatched().forEach(item ->
                history.add(new ViewingHistoryItem(
                        item.getTmdbId(),
                        item.getMediaType(),
                        item.getWatchedAt(),
                        null,
                        null,
                        item.getRuntimeMinutes()
                ))
        );
    }

    if (user.getWatchedEpisodes() != null) {
        user.getWatchedEpisodes().forEach(item ->
                history.add(new ViewingHistoryItem(
                        item.getTmdbId(),
                        "tv_episode",
                        item.getWatchedAt(),
                        item.getSeasonNumber(),
                        item.getEpisodeNumber(),
                        item.getRuntimeMinutes()
                ))
        );
    }

    history.sort(
            Comparator.comparing(
                    ViewingHistoryItem::getWatchedAt,
                    Comparator.nullsLast(Comparator.naturalOrder())
            )
    );

    return history;
}

public List<MonthlyStatsDTO> getMonthlyStats(String userId) {

    User user = userRepository.findById(userId).orElse(null);

    if (user == null) {
        return null;
    }

    // [0] movies, [1] TV series, [2] episodes, [3] viewing minutes
    Map<String, long[]> monthlyCounts = new TreeMap<>();

    if (user.getWatched() != null) {
        user.getWatched().forEach(item -> {
            String mediaType = item.getMediaType();

            if ("movie".equalsIgnoreCase(mediaType)) {
                addMonthlyCount(
                    monthlyCounts,
                    item.getWatchedAt(),
                    0,
                    item.getRuntimeMinutes()
                );
            } else if ("tv".equalsIgnoreCase(mediaType)) {
                addMonthlyCount(
                    monthlyCounts,
                    item.getWatchedAt(),
                    1,
                    null
                );
            }
        });
    }

    if (user.getWatchedEpisodes() != null) {
        user.getWatchedEpisodes().forEach(item ->
            addMonthlyCount(
                monthlyCounts,
                item.getWatchedAt(),
                2,
                item.getRuntimeMinutes()
            )
        );
    }

    List<MonthlyStatsDTO> result = new ArrayList<>();

    monthlyCounts.forEach((month, counts) -> {
        long totalActivities = counts[0] + counts[1] + counts[2];

        result.add(new MonthlyStatsDTO(
            month,
            counts[0],
            counts[1],
            counts[2],
            totalActivities,
            counts[3]
        ));
    });

    return result;
}

private void addMonthlyCount(
        Map<String, long[]> monthlyCounts,
        String watchedAt,
        int counterIndex,
        Integer runtimeMinutes) {

    if (watchedAt == null || watchedAt.isBlank()) {
        return;
    }

    try {
        String month = YearMonth.from(
            LocalDateTime.parse(watchedAt)
        ).toString();

        long[] counts = monthlyCounts.computeIfAbsent(
            month, key -> new long[4]
        );

        counts[counterIndex]++;

        if (runtimeMinutes != null && runtimeMinutes > 0) {
            counts[3] += runtimeMinutes;
        }

    } catch (DateTimeParseException exception) {
        // Ignore timestamps that cannot be parsed.
    }
}

public YearlyStatsDTO getYearlyStats(String userId, int year) {

    if (year < 1 || year > 9999) {
        throw new IllegalArgumentException(
            "Year must be between 1 and 9999."
        );
    }

    List<MonthlyStatsDTO> existingMonths = getMonthlyStats(userId);

    if (existingMonths == null) {
        return null;
    }

    Map<String, MonthlyStatsDTO> monthsByName = new java.util.HashMap<>();

    for (MonthlyStatsDTO month : existingMonths) {
        if (month.getMonth().startsWith(year + "-")) {
            monthsByName.put(month.getMonth(), month);
        }
    }

    List<MonthlyStatsDTO> monthlyBreakdown = new ArrayList<>();

    long totalMovies = 0;
    long totalTVSeries = 0;
    long totalEpisodes = 0;
    long totalViewingMinutes = 0;

    for (int monthNumber = 1; monthNumber <= 12; monthNumber++) {

        String monthName = String.format(
            "%04d-%02d", year, monthNumber
        );

        MonthlyStatsDTO month = monthsByName.get(monthName);

        if (month == null) {
            month = new MonthlyStatsDTO(
                monthName, 0, 0, 0, 0, 0
            );
        }

        monthlyBreakdown.add(month);

        totalMovies += month.getMoviesWatched();
        totalTVSeries += month.getTvSeriesWatched();
        totalEpisodes += month.getEpisodesWatched();
        totalViewingMinutes += month.getTotalViewingMinutes();
    }

    long totalActivities =
        totalMovies + totalTVSeries + totalEpisodes;

    return new YearlyStatsDTO(
        year,
        totalMovies,
        totalTVSeries,
        totalEpisodes,
        totalActivities,
        totalViewingMinutes,
        monthlyBreakdown
    );
}
}
