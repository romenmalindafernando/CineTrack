
package CineTrack.Backend.service;

import CineTrack.Backend.dto.GenreStatsDTO;
import CineTrack.Backend.model.User;
import CineTrack.Backend.model.WatchedItem;
import CineTrack.Backend.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GenreStatsService {

    private static final Logger logger =
            LoggerFactory.getLogger(GenreStatsService.class);

    private final UserRepository userRepository;
    private final RestClient tmdbClient;

    /*
     * In-memory cache of TMDB title details.
     * Cache keys use the media type and TMDB ID.
     */
    private final Map<String, Map<String, Object>> titleDetailsCache =
            new ConcurrentHashMap<>();

    public GenreStatsService(
            UserRepository userRepository,
            RestClient tmdbClient) {

        this.userRepository = userRepository;
        this.tmdbClient = tmdbClient;
    }

    public List<GenreStatsDTO> getGenreStats(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found: " + userId
                        )
                );

        Map<Integer, GenreStatsDTO> genreStatsMap =
                new HashMap<>();

        // User.java defines the watched list as getWatched().
        List<WatchedItem> watchedItems = user.getWatched();

        if (watchedItems == null || watchedItems.isEmpty()) {
            return new ArrayList<>();
        }

        for (WatchedItem watchedItem : watchedItems) {

            if (watchedItem == null) {
                continue;
            }

            int tmdbId = watchedItem.getTmdbId();
            String mediaType = watchedItem.getMediaType();

            if (mediaType == null || mediaType.isBlank()) {
                continue;
            }

            String normalizedMediaType =
                    mediaType.toLowerCase(Locale.ROOT);

            if (!normalizedMediaType.equals("movie")
                    && !normalizedMediaType.equals("tv")) {

                logger.warn(
                        "Skipping unsupported media type: {}",
                        mediaType
                );
                continue;
            }

            Map<String, Object> titleDetails;

            try {
                titleDetails = fetchTitleDetails(
                        tmdbId,
                        normalizedMediaType
                );
            } catch (Exception exception) {

                logger.warn(
                        "Could not retrieve TMDB details for {}:{}",
                        normalizedMediaType,
                        tmdbId,
                        exception
                );

                continue;
            }

            if (titleDetails == null) {
                continue;
            }

            Object genresObject = titleDetails.get("genres");

            if (!(genresObject instanceof List<?> genres)) {
                continue;
            }

            for (Object genreObject : genres) {

                if (!(genreObject instanceof Map<?, ?> genre)) {
                    continue;
                }

                Object genreIdObject = genre.get("id");
                Object genreNameObject = genre.get("name");

                if (!(genreIdObject instanceof Number genreIdNumber)
                        || !(genreNameObject instanceof String genreName)) {
                    continue;
                }

                int genreId = genreIdNumber.intValue();

                GenreStatsDTO stats =
                        genreStatsMap.computeIfAbsent(
                                genreId,
                                id -> new GenreStatsDTO(
                                        id,
                                        genreName,
                                        0L,
                                        0L,
                                        0L
                                )
                        );

                if (normalizedMediaType.equals("movie")) {

                    stats.setMoviesWatched(
                            stats.getMoviesWatched() + 1
                    );

                } else {

                    stats.setTvSeriesWatched(
                            stats.getTvSeriesWatched() + 1
                    );
                }

                stats.setTotalTitles(
                        stats.getTotalTitles() + 1
                );
            }
        }

        List<GenreStatsDTO> result =
                new ArrayList<>(genreStatsMap.values());

        // Sort by total watched titles, highest first.
        // getTotalTitles() returns long, so use comparingLong().
        result.sort(
                Comparator
                        .comparingLong(
                                GenreStatsDTO::getTotalTitles
                        )
                        .reversed()
                        .thenComparing(
                                GenreStatsDTO::getGenreName
                        )
        );

        return result;
    }

    /*
     * Fetch title details from TMDB and cache successful responses.
     * Cached details are reused for subsequent requests while the
     * application is running.
     */
    private Map<String, Object> fetchTitleDetails(
            int tmdbId,
            String mediaType) {

        String cacheKey =
                mediaType.toLowerCase(Locale.ROOT)
                        + ":"
                        + tmdbId;

        return titleDetailsCache.computeIfAbsent(
                cacheKey,
                key -> {

                    logger.info(
                            "Fetching title metadata from TMDB: {}",
                            key
                    );

                    return tmdbClient.get()
                            .uri(
                                    "/{mediaType}/{tmdbId}",
                                    mediaType,
                                    tmdbId
                            )
                            .retrieve()
                            .body(
                                    new ParameterizedTypeReference<
                                            Map<String, Object>>() {
                                    }
                            );
                }
        );
    }
}
