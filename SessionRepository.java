package kz.cinema.repository;

import kz.cinema.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByMovieId(Long movieId);
}