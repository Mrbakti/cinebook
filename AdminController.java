package kz.cinema.controller;

import kz.cinema.model.Session;
import kz.cinema.model.Movie;
import kz.cinema.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;
    private final SessionRepository sessionRepo;
    private final MovieRepository movieRepo;

    public AdminController(UserRepository userRepo, BookingRepository bookingRepo,
                           SessionRepository sessionRepo, MovieRepository movieRepo) {
        this.userRepo = userRepo;
        this.bookingRepo = bookingRepo;
        this.sessionRepo = sessionRepo;
        this.movieRepo = movieRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("userCount", userRepo.count());
        model.addAttribute("bookingCount", bookingRepo.count());
        model.addAttribute("movieCount", movieRepo.count());
        model.addAttribute("bookings", bookingRepo.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/sessions")
    public String sessions(Model model) {
        model.addAttribute("sessions", sessionRepo.findAll());
        model.addAttribute("movies", movieRepo.findAll());
        return "admin/sessions";
    }

    @PostMapping("/sessions/add")
    public String addSession(@RequestParam Long movieId, @RequestParam String hall,
                             @RequestParam String startTime, @RequestParam double price,
                             @RequestParam int seats) {
        Movie movie = movieRepo.findById(movieId).orElseThrow();
        Session s = new Session();
        s.setMovie(movie);
        s.setHall(hall);
        s.setStartTime(LocalDateTime.parse(startTime));
        s.setPrice(price);
        s.setTotalSeats(seats);
        s.setAvailableSeats(seats);
        sessionRepo.save(s);
        return "redirect:/admin/sessions";
    }

    @GetMapping("/sessions/delete/{id}")
    public String deleteSession(@PathVariable Long id) {
        sessionRepo.deleteById(id);
        return "redirect:/admin/sessions";
    }
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "admin/users";
    }
}