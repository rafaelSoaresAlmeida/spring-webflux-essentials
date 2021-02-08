package academy.devdojo.springwebfluxessentials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class SpringWebfluxEssentialsApplication {

//	static {
//		BlockHound.install(builder -> builder.allowBlockingCallsInside("java.util.UUID", "randomUUID"));
//	}

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxEssentialsApplication.class, args);

		// just to generate the encoded password
//		System.out.println("1: [" + PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("cansado") + "]");
//		System.out.println("2: [" + PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("polivalente") + "]");
	}

}
