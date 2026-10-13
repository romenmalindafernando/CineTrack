package CineTrack.Backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        String token = System.getenv("TMDB_API_TOKEN");

        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "TMDB_API_TOKEN environment variable is not set"
            );
        }

        return RestClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
