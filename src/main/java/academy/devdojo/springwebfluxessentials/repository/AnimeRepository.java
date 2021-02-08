package academy.devdojo.springwebfluxessentials.repository;

import academy.devdojo.springwebfluxessentials.entity.Anime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AnimeRepository extends ReactiveCrudRepository<Anime, Integer> {

    Mono<Anime> findById(final int id);
}
