package CineTrack.Backend.service;

import CineTrack.Backend.dto.ActorCreditDto;
import CineTrack.Backend.dto.ActorDetailsDto;
import CineTrack.Backend.dto.TmdbPersonCreditsResponse;
import CineTrack.Backend.dto.TmdbPersonDetailsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ActorService {
    private final RestClient restClient;

    @Value("${tmdb.api.token}")
    private String apiToken;

    public ActorService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .build();
    }

    public ActorDetailsDto getActorDetails(int actorId) {
        TmdbPersonDetailsResponse response = restClient.get()
                .uri("/person/" + actorId)
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .body(TmdbPersonDetailsResponse.class);

        if (response == null) {
            return null;
        }

        return new ActorDetailsDto(
                response.getId(),
                response.getName(),
                response.getBiography(),
                response.getBirthday(),
                response.getPlaceOfBirth(),
                response.getProfilePath(),
                response.getKnownForDepartment()
        );
    }

    public List<ActorCreditDto> getActorFilmography(int actorId) {
        TmdbPersonCreditsResponse response = restClient.get()
                .uri("/person/" + actorId + "/combined_credits")
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .body(TmdbPersonCreditsResponse.class);

        if (response == null || response.getCast() == null) {
            return List.of();
        }

        return response.getCast().stream()
                .filter(credit -> credit.getMediaType() != null
                        && (credit.getMediaType().equals("movie")
                        || credit.getMediaType().equals("tv")))
                .map(credit -> new ActorCreditDto(
                        credit.getId(),
                        credit.getTitle() != null
                                ? credit.getTitle() : credit.getName(),
                        credit.getMediaType(),
                        credit.getReleaseDate() != null
                                ? credit.getReleaseDate() : credit.getFirstAirDate(),
                        credit.getCharacter(),
                        credit.getVoteAverage(),
                        credit.getVoteCount(),
                        credit.getPosterPath(),
                        credit.getOverview()
                ))
                .toList();
    }
}
