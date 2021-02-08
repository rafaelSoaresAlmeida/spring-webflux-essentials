package academy.devdojo.springwebfluxessentials.configuration;

import academy.devdojo.springwebfluxessentials.entity.Anime;
import academy.devdojo.springwebfluxessentials.entity.User;
import academy.devdojo.springwebfluxessentials.repository.AnimeRepository;
import academy.devdojo.springwebfluxessentials.repository.UserRepository;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@Configuration
public class InitDatabase {

    @Autowired
    DatabaseClient client;

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

        return initializer;
    }

    @Bean
    public CommandLineRunner demo(AnimeRepository animeRepository, UserRepository userRepository) {

    //    initializeUserData();

        return (args) -> {
            // save a few customers
            animeRepository.saveAll(Arrays.asList(
                    Anime.builder().name("Full Metal").build(),
                    Anime.builder().name("Hellsing").build(),
                    Anime.builder().name("Attack Titan").build(),
                    Anime.builder().name("Zeoraima").build()))
                    .blockLast(Duration.ofSeconds(10));

            userRepository.saveAll(Arrays.asList(

                    User.builder().name("Capitao Desumano").username("cavalo").password("{bcrypt}$2a$10$B4JueaV/LHpVKSgw2skZteqT1m4OKIF8D6E/Vp1lqqJAl1xbZlyv.").authorities("ROLE_ADMIN,ROLE_USER").build(),
                    User.builder().name("Thaci").username("thacigod").password("{bcrypt}$2a$10$ScXipk72pD5kvpgcBBWqDuACWsX3VRchHyzPT05kOdarWLVXzIAm6").authorities("ROLE_USER").build()))
                    .blockLast(Duration.ofSeconds(10));
        };
    }

//    private void initializeUserData() {
//        this.client.sql(
//                "insert into user (id, name, user_name, password, authorities) values (1, 'Capitao Desumano', 'cavalo', '{bcrypt}$2a$10$NG3Rr87jps/cJaDzLVqikecA9XWftUKMH8qSIsKtpVedg1aFdAzFG', 'ROLE_ADMIN,ROLE_USER');"
//                        + "insert into user (id, name, user_name, password, authorities) values (2, 'Thaci', 'thacigod', '{bcrypt}$2a$10$/MOmwzM4IUymEnJGO8BE7e.pdwPhnqSLGQCYUALcfCP3YU8OUA3yu', 'ROLE_USER');"
//        )
//                .fetch()
//                .rowsUpdated()
//                .block();
//    }

}
