package CineTrack.Backend.service;

import CineTrack.Backend.dto.TmdbTvDetailsResponse;
import CineTrack.Backend.dto.TmdbTvEpisodeResponse;
import CineTrack.Backend.dto.TvEpisodeDetailsDto;
import CineTrack.Backend.dto.TmdbTvResponse;
import CineTrack.Backend.dto.TvDetailsDto;
import CineTrack.Backend.dto.TvDto;
import CineTrack.Backend.dto.TvSearchResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TvService {

    private final RestClient restClient;

    @Value("${tmdb.api.token}")
    private String apiToken;

    public TvService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .build();
    }

    public TvSearchResponseDto searchTv(String query) {

        TmdbTvResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/tv")
                        .queryParam("query", query)
                        .build())
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .body(TmdbTvResponse.class);

        if (response == null || response.getResults() == null) {
            return new TvSearchResponseDto(
                    1,
                    0,
                    0,
                    List.of()
            );
        }

        List<TvDto> tvShows = response.getResults()
                .stream()
                .map(tv -> new TvDto(
                        tv.getId(),
                        tv.getName(),
                        tv.getFirstAirDate(),
                        tv.getVoteAverage(),
                        tv.getPosterPath(),
                        tv.getOverview()
                ))
                .toList();

        return new TvSearchResponseDto(
                response.getPage(),
                response.getTotalPages(),
                response.getTotalResults(),
                tvShows
        );
    }

    public TvDetailsDto getTvDetails(int tvId) {

        TmdbTvDetailsResponse response = restClient.get()
                .uri("/tv/" + tvId)
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .body(TmdbTvDetailsResponse.class);

        if (response == null) {
            return null;
        }

        List<String> genres = response.getGenres()
                .stream()
                .map(TmdbTvDetailsResponse.Genre::getName)
                .toList();

        return new TvDetailsDto(
                response.getId(),
                response.getName(),
                response.getFirstAirDate(),
                response.getVoteAverage(),
                response.getNumberOfSeasons(),
                response.getNumberOfEpisodes(),
                response.getPosterPath(),
                response.getBackdropPath(),
                response.getOverview(),
                genres
        );
    }

    public TvEpisodeDetailsDto getEpisodeDetails(
            int tvId,
            int seasonNumber,
            int episodeNumber) {

        if (tvId <= 0 || seasonNumber < 0 || episodeNumber <= 0) {
            throw new IllegalArgumentException(
                    "TV ID and episode number must be positive; season number cannot be negative.");
        }

        TmdbTvEpisodeResponse response = restClient.get()
                .uri("/tv/" + tvId + "/season/" + seasonNumber
                        + "/episode/" + episodeNumber)
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .body(TmdbTvEpisodeResponse.class);

        if (response == null) {
            return null;
        }

        return new TvEpisodeDetailsDto(
                response.getId(),
                response.getName(),
                response.getOverview(),
                response.getSeasonNumber(),
                response.getEpisodeNumber(),
                response.getAirDate(),
                response.getStillPath(),
                response.getVoteAverage(),
                response.getRuntime()
        );
    }
}
