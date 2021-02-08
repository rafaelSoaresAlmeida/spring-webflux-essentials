package academy.devdojo.springwebfluxessentials.util;

import academy.devdojo.springwebfluxessentials.entity.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved() {
        return Anime.builder()
                .name("Tensei Shitara")
                .build();
    }

    public static Anime createValidAnime() {
        return Anime.builder()
                .id(1)
                .name("Tensei Shitara")
                .build();
    }

    public static Anime createValidUpdateAnime() {
        return Anime.builder()
                .id(1)
                .name("Tensei Shitara 2")
                .build();
    }
}
