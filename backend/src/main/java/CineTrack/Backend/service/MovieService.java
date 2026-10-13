package CineTrack.Backend.service;

import CineTrack.Backend.dto.MovieDetailsDto;
import CineTrack.Backend.dto.MovieDto;
import CineTrack.Backend.dto.MovieSearchResponseDto;
import CineTrack.Backend.dto.TmdbMovieDetailsResponse;
import CineTrack.Backend.dto.TmdbMovieResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class MovieService {

    private final RestClient restClient;

    @Value("${tmdb.api.token}")
    private String apiToken;

    public MovieService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .build();
    }

    public MovieSearchResponseDto searchMovies(String query) {

        TmdbMovieResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("query", query)
                        .build())
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .body(TmdbMovieResponse.class);

        if (response == null || response.getResults() == null) {
            return new MovieSearchResponseDto(
                    1,
                    0,
                    0,
                    List.of()
            );
        }

        List<MovieDto> movies = response.getResults()
                .stream()
                .map(movie -> new MovieDto(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getReleaseDate(),
                        movie.getVoteAverage(),
                        movie.getPosterPath(),
                        movie.getOverview()
                ))
                .toList();

        return new MovieSearchResponseDto(
                response.getPage(),
                response.getTotalPages(),
                response.getTotalResults(),
                movies
        );
    }

    public MovieDetailsDto getMovieDetails(int movieId) {

        TmdbMovieDetailsResponse response = restClient.get()
                .uri("/movie/" + movieId)
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .body(TmdbMovieDetailsResponse.class);

        if (response == null) {
            return null;
        }

        List<String> genres = response.getGenres()
                .stream()
                .map(TmdbMovieDetailsResponse.Genre::getName)
                .toList();

        return new MovieDetailsDto(
                response.getId(),
                response.getTitle(),
                response.getReleaseDate(),
                response.getVoteAverage(),
                response.getRuntime(),
                response.getPosterPath(),
                response.getBackdropPath(),
                response.getOverview(),
                genres
        );
    }
}