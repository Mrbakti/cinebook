package kz.cinema.controller;

import kz.cinema.model.User;
import kz.cinema.repository.UserRepository;
import kz.cinema.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    public AuthController(UserRepository userRepo, PasswordEncoder encoder, EmailService emailService) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @GetMapping("/register")
    public String registerPage() { return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setRole("USER");
        userRepo.save(user);

        emailService.sendEmail(email, "Welcome to CineBook",
            "Hello " + username + "! Your account has been created successfully.");

        return "redirect:/login?registered";
    }
}