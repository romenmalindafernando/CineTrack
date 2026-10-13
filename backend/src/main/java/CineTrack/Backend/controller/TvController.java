package CineTrack.Backend.controller;

import CineTrack.Backend.dto.TvDetailsDto;
import CineTrack.Backend.dto.TvEpisodeDetailsDto;
import CineTrack.Backend.dto.TvSearchResponseDto;
import CineTrack.Backend.service.TvService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tv")
public class TvController {

    private final TvService tvService;

    public TvController(TvService tvService) {
        this.tvService = tvService;
    }

    @GetMapping("/search")
    public TvSearchResponseDto searchTv(@RequestParam String query) {
        return tvService.searchTv(query);
    }

    @GetMapping("/{tvId}")
    public TvDetailsDto getTvDetails(
            @PathVariable int tvId) {

        return tvService.getTvDetails(tvId);
    }

    @GetMapping("/{tvId}/season/{seasonNumber}/episode/{episodeNumber}")
    public TvEpisodeDetailsDto getEpisodeDetails(
            @PathVariable int tvId,
            @PathVariable int seasonNumber,
            @PathVariable int episodeNumber) {

        return tvService.getEpisodeDetails(
                tvId, seasonNumber, episodeNumber);
    }
}
