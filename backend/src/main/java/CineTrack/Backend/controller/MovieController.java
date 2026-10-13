package CineTrack.Backend.controller;

import CineTrack.Backend.dto.MovieSearchResponseDto;
import CineTrack.Backend.service.MovieService;
import CineTrack.Backend.service.RecommendationService;
import CineTrack.Backend.dto.MovieRecommendationDto;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import CineTrack.Backend.dto.MovieDetailsDto;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

@GetMapping("/{movieId}")
public MovieDetailsDto getMovieDetails(
        @PathVariable int movieId) {

    return movieService.getMovieDetails(movieId);
}

    private final MovieService movieService;
    private final RecommendationService recommendationService;

    public MovieController(MovieService movieService, RecommendationService recommendationService) {
        this.movieService = movieService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/popular")
    public MovieSearchResponseDto getPopularMovies() {
        return movieService.getPopularMoviesForDisplay();
    }
    @GetMapping("/search")
    public MovieSearchResponseDto searchMovies(
            @RequestParam String query) {

        return movieService.searchMovies(query);
    }

    @GetMapping("/recommendations")
    public List<MovieRecommendationDto> getRecommendations(
            @RequestParam String userId,
            @RequestParam(defaultValue = "12") int limit) {
        return recommendationService.getRecommendations(userId, limit);
    }
}
