package kz.cinema.service;

import kz.cinema.model.*;
import kz.cinema.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepo;
    @Autowired private SessionRepository sessionRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private EmailService emailService;

    public Booking book(String username, Long sessionId, int seats) {
        User user = userRepo.findByUsername(username).orElseThrow();
        Session session = sessionRepo.findById(sessionId).orElseThrow();

        if (session.getAvailableSeats() < seats) throw new RuntimeException("Not enough seats");

        session.setAvailableSeats(session.getAvailableSeats() - seats);
        sessionRepo.save(session);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSession(session);
        booking.setSeats(seats);
        booking.setTotalPrice(seats * session.getPrice());
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        bookingRepo.save(booking);

        emailService.sendEmail(user.getEmail(),
            "Booking Confirmed - " + session.getMovie().getTitle(),
            "Your booking is confirmed!\nMovie: " + session.getMovie().getTitle() +
            "\nSeats: " + seats + "\nTotal: " + booking.getTotalPrice() + " KZT");

        return booking;
    }

    public List<Booking> getUserBookings(String username) {
        User user = userRepo.findByUsername(username).orElseThrow();
        return bookingRepo.findByUser(user);
    }
}