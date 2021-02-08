package academy.devdojo.springwebfluxessentials.controller;

import academy.devdojo.springwebfluxessentials.entity.Anime;
import academy.devdojo.springwebfluxessentials.service.AnimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("animes")
@SecurityScheme(
        name = "Basic Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all animes",
            security = @SecurityRequirement(name = "Basic Authentication"),
            tags = {"anime"})
    public Flux<Anime> listAll() {
        log.info("Call list all animes");
        return animeService.findAll();
    }

    /**
     * @param trace, if you put a query string "trace=true" - exemple: http://localhost:8080/animes/18888?trace=true and a exception occurred,
     *               the response will have
     *               exception trace inside body response.
     * @param id
     * @return
     */
    @GetMapping(path = "{id}")
    @Operation(summary = "Find a anime by id",
            security = @SecurityRequirement(name = "Basic Authentication"),
            tags = {"anime"})
    public Mono<Anime> findById(@PathVariable final int id) {
        return animeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save a new anime in database",
            security = @SecurityRequirement(name = "Basic Authentication"),
            tags = {"anime"})
    public Mono<Anime> save(@Valid @RequestBody final Anime anime){
        return  animeService.save(anime);
    }

    @PostMapping(path = "batch")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save a list of new animes in database",
            security = @SecurityRequirement(name = "Basic Authentication"),
            tags = {"anime"})
    public Flux<Anime> saveBatch(@RequestBody final List<Anime> animes){
        return  animeService.saveAll(animes);
    }

    @PutMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update an anime that already exist in database",
            security = @SecurityRequirement(name = "Basic Authentication"),
            tags = {"anime"})
    public Mono<Void> update(@PathVariable final int id, @Valid @RequestBody final Anime anime){
        return  animeService.update(anime.withId(id));
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an anime in database",
            security = @SecurityRequirement(name = "Basic Authentication"),
            tags = {"anime"})
    public Mono<Void> delete(@PathVariable final int id){
        return  animeService.delete(id);
    }
}
