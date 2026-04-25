package kz.cinema;

import kz.cinema.model.*;
import kz.cinema.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;

@SpringBootApplication
public class CinemaApplication {
    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository users, MovieRepository movies,
                           SessionRepository sessions, PasswordEncoder encoder) {
        return args -> {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@cinema.kz");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRole("ADMIN");
            users.save(admin);

            Movie m1 = new Movie();
            m1.setTitle("Dune: Part Two");
            m1.setGenre("Sci-Fi");
            m1.setDuration(167);
            m1.setDescription("The epic conclusion of Paul Atreides journey.");
            movies.save(m1);

            Movie m2 = new Movie();
            m2.setTitle("Oppenheimer");
            m2.setGenre("Drama");
            m2.setDuration(180);
            m2.setDescription("The story of the atomic bomb creator.");
            movies.save(m2);

            Movie m3 = new Movie();
            m3.setTitle("Deadpool & Wolverine");
            m3.setGenre("Action");
            m3.setDuration(128);
            m3.setDescription("Two anti-heroes team up in a chaotic adventure.");
            movies.save(m3);

            for (Movie movie : movies.findAll()) {
                for (int i = 0; i < 3; i++) {
                    Session s = new Session();
                    s.setMovie(movie);
                    s.setHall("Hall " + (i + 1));
                    s.setStartTime(LocalDateTime.now().plusDays(i + 1).withHour(14 + i * 2).withMinute(0));
                    s.setPrice(1500.0 + i * 500);
                    s.setTotalSeats(50);
                    s.setAvailableSeats(50);
                    sessions.save(s);
                }
            }
        };
    }
}