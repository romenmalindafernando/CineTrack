package CineTrack.Backend.controller;

import CineTrack.Backend.dto.ActorCreditDto;
import CineTrack.Backend.dto.ActorDetailsDto;
import CineTrack.Backend.service.ActorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {
    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping("/{actorId}")
    public ActorDetailsDto getActorDetails(@PathVariable int actorId) {
        return actorService.getActorDetails(actorId);
    }

    @GetMapping("/{actorId}/filmography")
    public List<ActorCreditDto> getActorFilmography(@PathVariable int actorId) {
        return actorService.getActorFilmography(actorId);
    }
}
