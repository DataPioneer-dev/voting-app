package zhar.achraf.voting_system_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zhar.achraf.voting_system_app.entity.User;
import zhar.achraf.voting_system_app.repo.UserRepo;
import zhar.achraf.voting_system_app.service.UserService;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "auth";
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user, Model model) {
        userService.registerUser(user);
        return "auth";
    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            model.addAttribute("error", "The email you entered is not registered with our system. Please sign up if you are new!");
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Incorrect password. Please try again.");
        } else {
            return "redirect:/index"; // Redirect to index page
        }

        model.addAttribute("user", new User());
        return "auth";
    }
}
