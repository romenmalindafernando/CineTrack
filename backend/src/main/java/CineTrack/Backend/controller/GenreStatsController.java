
package CineTrack.Backend.controller;

import CineTrack.Backend.dto.GenreStatsDTO;
import CineTrack.Backend.service.GenreStatsService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class GenreStatsController {

    private final GenreStatsService genreStatsService;

    public GenreStatsController(
            GenreStatsService genreStatsService) {
        this.genreStatsService = genreStatsService;
    }

    @GetMapping("/{userId}/stats/genres")
    public List<GenreStatsDTO> getGenreStats(
            @PathVariable String userId) {

        List<GenreStatsDTO> stats =
            genreStatsService.getGenreStats(userId);

        if (stats == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            );
        }

        return stats;
    }
}
