package academy.devdojo.springwebfluxessentials.controller;

import academy.devdojo.springwebfluxessentials.entity.Anime;
import academy.devdojo.springwebfluxessentials.service.AnimeService;
import academy.devdojo.springwebfluxessentials.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeService;

    private Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void blockHoundSetup() {
        //  BlockHound.install();
    }

    @BeforeEach
    public void setup() {
        BDDMockito.when(animeService.findAll())
                .thenReturn(Flux.just(anime));

        BDDMockito.when(animeService.findById(1))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeService.save(AnimeCreator.createAnimeToBeSaved()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeService.saveAll(List.of(AnimeCreator.createAnimeToBeSaved(), AnimeCreator.createAnimeToBeSaved())))
                .thenReturn(Flux.just(anime, anime));

        BDDMockito.when(animeService.delete(1))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeService.save(AnimeCreator.createValidAnime()))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeService.update(AnimeCreator.createValidAnime()))
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
    @DisplayName("ListAll returns a flux of anime")
    public void listAll_ReturnFluxOfAnime_whenSuccessful() {
        StepVerifier.create(animeController.listAll())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("FindById returns a Mono with anime if exists")
    public void findById_ReturnMonoOfAnime_whenSuccessful() {
        StepVerifier.create(animeController.findById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("Save creates an anime when successful")
    public void save_CreatesAnime_whenSuccessful() {
        final Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        StepVerifier.create(animeController.save(animeToBeSaved))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("SaveBatch creates a list of anime when successful")
    public void saveBatch_CreatesListAnime_whenSuccessful() {
        final Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        StepVerifier.create(animeController.saveBatch(List.of(animeToBeSaved, animeToBeSaved)))
                .expectSubscription()
                .expectNext(anime, anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete removes the anime when successful")
    public void delete_RemovesAnime_whenSuccessful() {
        StepVerifier.create(animeController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("Update save updated anime and returns empty mono when successful")
    public void update_SaveUpdateAnime_whenSuccessful() {
        StepVerifier.create(animeController.update(1, AnimeCreator.createAnimeToBeSaved()))
                .expectSubscription()
                .verifyComplete();
    }

}