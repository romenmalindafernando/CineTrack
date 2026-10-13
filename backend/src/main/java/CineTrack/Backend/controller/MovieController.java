package CineTrack.Backend.controller;

import CineTrack.Backend.dto.MovieSearchResponseDto;
import CineTrack.Backend.service.MovieService;
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

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public MovieSearchResponseDto searchMovies(
            @RequestParam String query) {

        return movieService.searchMovies(query);
    }
}