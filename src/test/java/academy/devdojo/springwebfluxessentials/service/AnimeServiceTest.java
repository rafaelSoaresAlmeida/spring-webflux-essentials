package academy.devdojo.springwebfluxessentials.service;

import academy.devdojo.springwebfluxessentials.entity.Anime;
import academy.devdojo.springwebfluxessentials.repository.AnimeRepository;
import academy.devdojo.springwebfluxessentials.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

    private Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void blockHoundSetup() {
        //  BlockHound.install();
    }

    @BeforeEach
    public void setup() {
        BDDMockito.when(animeRepository.findAll())
                .thenReturn(Flux.just(anime));

        BDDMockito.when(animeRepository.findById(1))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeRepository.findById(99))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeRepository.save(AnimeCreator.createAnimeToBeSaved()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeRepository.saveAll(List.of(AnimeCreator.createAnimeToBeSaved(), AnimeCreator.createAnimeToBeSaved())))
                .thenReturn(Flux.just(anime, anime));

        BDDMockito.when(animeRepository.delete(ArgumentMatchers.any(Anime.class)))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeRepository.save(AnimeCreator.createValidAnime()))
                .thenReturn(Mono.empty());
    }

    //@Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @Test
    @DisplayName("FindAll returns a flux of anime")
    public void findAll_ReturnFluxOfAnime_whenSuccessful() {
        StepVerifier.create(animeService.findAll())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("FindById returns a Mono with anime if exists")
    public void findById_ReturnMonoOfAnime_whenSuccessful() {
        StepVerifier.create(animeService.findById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("FindById returns a Mono Error when anime does not exist")
    public void findById_ReturnMonoOfError_whenEmptyMonoIsReturned() {
        StepVerifier.create(animeService.findById(99))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("Save creates an anime when successful")
    public void save_CreatesAnime_whenSuccessful() {
        final Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        StepVerifier.create(animeService.save(animeToBeSaved))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("SaveAll creates a list of anime when successful")
    public void saveAll_CreatesListAnime_whenSuccessful() {
        final Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        StepVerifier.create(animeService.saveAll(List.of(animeToBeSaved, animeToBeSaved)))
                .expectSubscription()
                .expectNext(anime, anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("SaveAll return Mono error when one of the objects in the list contains null or empty name")
    public void saveAll_ReturnsMonoError_whenContainsInvalidName() {
        final Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();

        BDDMockito.when(animeRepository.saveAll(ArgumentMatchers.anyIterable()))
                .thenReturn(Flux.just(anime, anime.withName("")));

        StepVerifier.create(animeService.saveAll(List.of(animeToBeSaved, animeToBeSaved.withName(""))))
                .expectSubscription()
                .expectNext(anime)
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("Delete removes the anime when successful")
    public void delete_RemovesAnime_whenSuccessful() {
        StepVerifier.create(animeService.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete returns Mono error when anime does not exist")
    public void delete_ReturnMonoError_WhenEmptyMonoIsReturned() {
        StepVerifier.create(animeService.delete(99))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("Update save updated anime and returns empty mono when successful")
    public void update_SaveUpdateAnime_whenSuccessful() {
        StepVerifier.create(animeService.update(AnimeCreator.createValidAnime()))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("Update returns Mono Error when anime does not exist")
    public void update_ReturnMonoError_whenEmptyMonoIsReturned() {
        final Anime animeError = Anime.builder().id(99).name("Error").build();
        StepVerifier.create(animeService.update(animeError))
                .expectError(ResponseStatusException.class)
                .verify();
    }

}