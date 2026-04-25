package kz.cinema.controller;

import kz.cinema.repository.MovieRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MovieRepository movieRepo;

    public HomeController(MovieRepository movieRepo) {
        this.movieRepo = movieRepo;
    }

    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/movies")
    public String movies(Model model) {
        model.addAttribute("movies", movieRepo.findAll());
        return "movies";
    }
}