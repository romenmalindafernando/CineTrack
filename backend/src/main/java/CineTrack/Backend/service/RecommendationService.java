package CineTrack.Backend.service;

import CineTrack.Backend.dto.MovieRecommendationDto;
import CineTrack.Backend.dto.TmdbMovieResponse;
import CineTrack.Backend.model.Review;
import CineTrack.Backend.model.User;
import CineTrack.Backend.model.WatchedItem;
import CineTrack.Backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RecommendationService {
    private final UserRepository userRepository;
    private final MovieService movieService;

    public RecommendationService(
            UserRepository userRepository, MovieService movieService) {
        this.userRepository = userRepository;
        this.movieService = movieService;
    }

    public List<MovieRecommendationDto> getRecommendations(
            String userId, int requestedLimit) {
        int limit = Math.max(1, Math.min(requestedLimit, 30));
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of();

        Set<Integer> watchedIds = new HashSet<>();
        if (user.getWatched() != null) {
            for (WatchedItem item : user.getWatched()) {
                if ("movie".equalsIgnoreCase(item.getMediaType())) {
                    watchedIds.add(item.getTmdbId());
                }
            }
        }

        Map<Integer, Candidate> candidates = new LinkedHashMap<>();
        Map<Integer, Double> scores = new HashMap<>();
        Map<Integer, String> reasons = new HashMap<>();

        List<Review> reviews = user.getReviews() == null
                ? new ArrayList<>() : new ArrayList<>(user.getReviews());

        reviews.removeIf(r -> !"movie".equalsIgnoreCase(r.getMediaType()));
        reviews.sort(Comparator.comparingDouble(
                (Review r) -> Math.abs(r.getRating() - 5.0)).reversed());

        int processed = 0;
        for (Review review : reviews) {
            if (processed >= 10) break;

            double preference = (review.getRating() - 5.0) / 5.0;
            if (Math.abs(preference) < 0.2) continue;

            try {
                TmdbMovieResponse response =
                        movieService.getMovieRecommendations(review.getTmdbId());
                if (response == null || response.getResults() == null) continue;
                processed++;

                for (TmdbMovieResponse.TmdbMovie movie : response.getResults()) {
                    if (movie.getId() <= 0 || watchedIds.contains(movie.getId())) continue;

                    candidates.putIfAbsent(movie.getId(), new Candidate(
                            movie.getId(), movie.getTitle(), movie.getReleaseDate(),
                            movie.getVoteAverage(), movie.getPosterPath(), movie.getOverview()));

                    double quality = Math.max(0, Math.min(10, movie.getVoteAverage())) / 10.0;
                    scores.merge(movie.getId(), preference * (0.5 + quality), Double::sum);

                    if (preference > 0) {
                        reasons.putIfAbsent(movie.getId(),
                                "Because you rated a similar movie highly");
                    }
                }
            } catch (RestClientException ignored) {
                // Continue if an individual TMDB request fails.
            }
        }

        if (scores.values().stream().filter(score -> score > 0).count() < limit) {
            try {
                TmdbMovieResponse popular = movieService.getPopularMovies();
                if (popular != null && popular.getResults() != null) {
                    for (TmdbMovieResponse.TmdbMovie movie : popular.getResults()) {
                        if (movie.getId() <= 0 || watchedIds.contains(movie.getId())) continue;

                        candidates.putIfAbsent(movie.getId(), new Candidate(
                                movie.getId(), movie.getTitle(), movie.getReleaseDate(),
                                movie.getVoteAverage(), movie.getPosterPath(), movie.getOverview()));

                        double popularityScore =
                                Math.max(0, Math.min(10, movie.getVoteAverage())) / 10.0;
                        Double existingScore = scores.get(movie.getId());

                        if (existingScore == null || existingScore <= 0) {
                            scores.put(movie.getId(), popularityScore);
                            reasons.put(movie.getId(), "Popular with movie fans");
                        } else {
                            reasons.putIfAbsent(movie.getId(), "Popular with movie fans");
                        }
                    }
                }
            } catch (RestClientException ignored) {
                // Keep any candidates already collected.
            }
        }

        return candidates.values().stream()
                .filter(m -> scores.getOrDefault(m.id(), 0.0) > 0)
                .sorted(Comparator
                        .comparingDouble((Candidate m) -> scores.getOrDefault(m.id(), 0.0))
                        .reversed()
                        .thenComparing(Candidate::rating, Comparator.reverseOrder()))
                .limit(limit)
                .map(m -> new MovieRecommendationDto(
                        m.id(), m.title(), m.releaseDate(), m.rating(),
                        m.posterPath(), m.overview(),
                        Math.round(scores.getOrDefault(m.id(), 0.0) * 100.0) / 100.0,
                        reasons.getOrDefault(m.id(), "Recommended for you")))
                .toList();
    }

    private record Candidate(
            int id, String title, String releaseDate, double rating,
            String posterPath, String overview) {}
}
