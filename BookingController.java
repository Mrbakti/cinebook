package kz.cinema.controller;

import kz.cinema.model.Session;
import kz.cinema.repository.SessionRepository;
import kz.cinema.service.BookingService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/booking")
public class BookingController {

    private final SessionRepository sessionRepo;
    private final BookingService bookingService;

    public BookingController(SessionRepository sessionRepo, BookingService bookingService) {
        this.sessionRepo = sessionRepo;
        this.bookingService = bookingService;
    }

    @GetMapping("/movie/{movieId}")
    public String bookingPage(@PathVariable Long movieId, Model model) {
        try {
            List<Session> sessions = sessionRepo.findByMovieId(movieId);
            model.addAttribute("sessions", sessions);
            return "booking";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("sessions", List.of());
            return "booking";
        }
    }

    @PostMapping("/confirm")
    public String confirm(@RequestParam Long sessionId,
                          @RequestParam int seats,
                          Authentication auth,
                          Model model) {
        try {
            bookingService.book(auth.getName(), sessionId, seats);
            return "redirect:/booking/my";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/booking/my";
        }
    }

    @GetMapping("/my")
    public String myBookings(Model model, Authentication auth) {
        try {
            model.addAttribute("bookings", bookingService.getUserBookings(auth.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("bookings", List.of());
        }
        return "my-bookings";
    }
}