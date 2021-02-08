package academy.devdojo.springwebfluxessentials.service;

import academy.devdojo.springwebfluxessentials.entity.Anime;
import academy.devdojo.springwebfluxessentials.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Flux<Anime> findAll() {
        return animeRepository.findAll();
    }

    public Mono<Anime> findById(final int id) {
        return animeRepository.findById(id).switchIfEmpty(monoResponseStatusNotFoundException(HttpStatus.NOT_FOUND, "Anime not found"));
    }

    public <T> Mono<T> monoResponseStatusNotFoundException(final HttpStatus status, final String message) {
        return Mono.error(new ResponseStatusException(status, message));
    }

    public Mono<Anime> save(final Anime anime) {
        return animeRepository.save(anime);
    }

    @Transactional
    public Flux<Anime> saveAll(List<Anime> animes) {
        return animeRepository.saveAll(animes)
                .doOnNext(this::throwResponseStatusExceptionWhenEmptyName);
    }

    public Mono<Void> update(final Anime anime) {
        return findById(anime.getId())
                .map(animeFound -> anime.withId(animeFound.getId()))
                .flatMap(animeRepository::save)
                .then();
    }

    public Mono<Void> delete(final int id) {
        return findById(id)
                .flatMap(animeRepository::delete);
    }

    private void throwResponseStatusExceptionWhenEmptyName(final Anime anime) {
        if (StringUtils.isBlank(anime.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Name");
        }
    }

}
